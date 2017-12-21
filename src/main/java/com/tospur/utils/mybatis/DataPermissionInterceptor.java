package com.tospur.utils.mybatis;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.MappedStatement.Builder;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.transaction.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.github.miemiedev.mybatis.paginator.OffsetLimitInterceptor.BoundSqlSqlSource;

@Intercepts({@Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class,
        RowBounds.class, ResultHandler.class})})
public class DataPermissionInterceptor implements Interceptor {


    private final String tagName = "{promission";

    private final String tagEnd = "}";

    private final String field = "field";

    private final String dataType = "dataType";

    private final static Map<String, String> fields = new HashMap<String, String>() {
        private static final long serialVersionUID = -2688071631258124257L;

        {
            put("region", "region_id");
            put("subcompany", "subcompany_id");
            put("branch", "branch_id");
            put("staff", "weok_no");
        }
    };

    private final static Map<String, String> tableName = new HashMap<String, String>() {
        private static final long serialVersionUID = 6162204713146197183L;

        {
            put("region", "base_role_region");
            put("subcompany", "base_role_subcompany");
            put("branch", "base_role_branch");
            put("staff", "base_role_staff");
        }
    };

    private static final Logger LOGGER = LoggerFactory.getLogger(DataPermissionInterceptor.class);
    static int MAPPED_STATEMENT_INDEX = 0;
    static int PARAMETER_INDEX = 1;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        final Executor executor = (Executor) invocation.getTarget();
        final Object[] queryArgs = invocation.getArgs();
        final MappedStatement ms = (MappedStatement) queryArgs[MAPPED_STATEMENT_INDEX];
        final Object parameter = queryArgs[PARAMETER_INDEX];
        String sql = ms.getBoundSql(parameter).getSql().trim();
        String token = getUserToken();
        List<ParameterMapping> parameterMappings = new ArrayList<ParameterMapping>(
                ms.getBoundSql(parameter).getParameterMappings());
        //如果没有token，证明是登录或其他特殊接口，放过此类接口不做处理
        if (token == null) {
            return invocation.proceed();
        }
        try {
            //重组SQL
            String newSql = this.reBuildSql(sql, executor, token);
            //将修改后的SQL提交给mybatis
            queryArgs[MAPPED_STATEMENT_INDEX] = this.copyFromNewSql(ms, ms.getBoundSql(parameter), newSql, parameterMappings, parameterMappings);
            System.out.println(queryArgs[MAPPED_STATEMENT_INDEX]);
        } catch (Exception e) {
            LOGGER.error("mybatis权限过滤器报错", e);
        }

        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }

    public String getUserToken() {
        if (RequestContextHolder.getRequestAttributes() == null) {
            return null;
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        if (request == null) {
            return null;
        }
        String token = request.getHeader("X-Token");
        return token;
    }

    public String reBuildSql(String oldSql, Executor executor, String token) {
        StringBuffer newSql = new StringBuffer();
        if (!StringUtils.isEmpty(oldSql)) {
            //新增 ：  sql中是否存在union合表  如果存在 拆分做数据where拼接处理
            if (oldSql.toLowerCase().contains("union")) {
                String[] oldSqls = oldSql.split("(?i)union");
                ///新增 ：  以每一段union  拆分为一条sql处理 循环拼接数据权限where sql  然后拼接在一起
                for (int i = 0; i < oldSqls.length; i++) {
                    if (i == 0) {
                        newSql.append(appendPermissionSql(oldSqls[i], executor, token));
                    } else {
                        newSql.append(" union " + appendPermissionSql(oldSqls[i], executor, token));
                    }
                }
            } else {
                //不存在合表  继续处理
                newSql.append(appendPermissionSql(oldSql, executor, token));
            }
        } else {
            return oldSql;
        }
        return newSql.toString();
    }


    public String appendPermissionSql(String oldSql, Executor executor, String token) {
        StringBuffer newSql = new StringBuffer("");
        int fromIndex = 0;
        boolean firstFlag = true;
        boolean hasData = false;
        if (oldSql.contains(tagName)) {
            //获取工号
            String workNo = token.substring(token.indexOf("#") + 1);
            // 拼接标签出现前的sql
            newSql.append(oldSql.substring(0, oldSql.indexOf(tagName, 1)));
            //首先判断该用户是否管理员
            int isAdmin = this.getIsAdminByWorkNo(executor.getTransaction(), workNo);
            if (isAdmin > 0 || workNo.equals("admin")) {
                //管理员用户 直接返回sql不做查询条件拼接
                newSql.append(oldSql.substring(oldSql.lastIndexOf(tagEnd) + 1));
                return newSql.toString();
            }
            // 算出标签出现次数
            int count = this.count(oldSql, tagName);
            for (int i = 1; i <= count; i++) {
                int beginIndex = oldSql.indexOf(tagName, fromIndex) + tagName.length();
                int endIndex = oldSql.indexOf(tagEnd, fromIndex);
                String tag = oldSql.substring(beginIndex, endIndex);
                fromIndex = oldSql.indexOf(tagEnd, fromIndex) + 1;
                // 解析标签
                Map<String, String> tmap = this.resolveTagToMap(tag);
                // 生成SQL语句并查询结果集
                List<String> datas = this.getPromissionData(executor.getTransaction(), tmap.get(dataType), workNo);
                // 结果集非空的话就开始拼接SQL语句了
                if (!datas.isEmpty()) {
                    hasData = true;
                    // 是否第一次拼接标记，如果第一次拼接需要加入关系符 "and (" 否则加入 or
                    if (firstFlag) {
                        // 修改标记状态
                        firstFlag = false;
                        // 判断当前语句中是否包含where
                        if (oldSql.toLowerCase().contains("where")) {
                            newSql.append(" AND (");
                        } else {
                            newSql.append(" WHERE (");
                        }
                    } else {
                        newSql.append(" OR ");
                    }
                    newSql.append(tmap.get(field)).append(" IN (");
                    for (String data : datas) {
                        newSql.append("'").append(data).append("'").append(",");
                    }
                    // 去除最后的逗号
                    newSql.deleteCharAt(newSql.length() - 1);
                    newSql.append(") ");
                }
            }
            if (hasData) {
                newSql.append(")");
            }
            if (!hasData && count >= 1) {
                // 判断当前语句中是否包含where
                if (oldSql.toLowerCase().contains("where")) {
                    newSql.append(" AND 1=2");
                } else {
                    newSql.append(" WHERE 1=2");
                }
            }
            // 拼接标签之后的SQL语句
            newSql.append(oldSql.substring(oldSql.lastIndexOf(tagEnd) + 1));
            return newSql.toString();
        } else {
            return oldSql;
        }
    }

    private int getIsAdminByWorkNo(final Transaction transaction, String workNo) {
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT SUM(t.c) FROM ( SELECT COUNT(1) AS 'c' FROM base_user u WHERE u.`category` = 1 AND u.`work_no` = ").append("'").append(workNo).append("'");
        sql.append(" UNION ALL SELECT COUNT(1) AS 'c' FROM base_role r LEFT JOIN base_user_role ur ON ur.role_id = r.role_id");
        sql.append(" WHERE r.`category` = 1 AND ur.`work_no` = ").append("'").append(workNo).append("' ) t");
        Connection connection;
        int res = 0;
        try {
            connection = transaction.getConnection();
            PreparedStatement resStmt = connection.prepareStatement(sql.toString());
            ResultSet rs = resStmt.executeQuery();
            while (rs.next()) {
                res = rs.getInt(1);
            }
        } catch (SQLException e) {
            LOGGER.error("生成判断是否管理员SQL异常 sql ：" + sql.toString(), e);
        }

        return res;
    }

    public int count(String text, String sub) {
        int count = 0, start = 0;
        while ((start = text.indexOf(sub, start)) >= 0) {
            start += sub.length();
            count++;
        }
        return count;
    }

    // 解析标签生成Map
    public Map<String, String> resolveTagToMap(String tag) {
        Map<String, String> result = new HashMap<String, String>();
        String[] temp = tag.split(",");
        for (int i = 0; i < temp.length; i++) {
            result.put(temp[i].split("=")[0].trim(), temp[i].split("=")[1].trim());
        }
        return result;
    }

    public List<String> getPromissionData(final Transaction transaction, String type, String workNo) {
        //获取用户所有角色
        StringBuffer getRole = new StringBuffer("");
        getRole.append("SELECT u.role_id FROM base_user_role u WHERE u.work_no = '").append(workNo).append("'");

        StringBuffer sql = new StringBuffer("");
        Connection connection;
        List<String> roles = new ArrayList<String>();
        List<String> res = new ArrayList<String>();
        try {
            connection = transaction.getConnection();
            PreparedStatement resStmt = connection.prepareStatement(getRole.toString());
            ResultSet rs = resStmt.executeQuery();
            while (rs.next()) {
                roles.add(rs.getString(1));
            }
            if (!roles.isEmpty()) {
                sql.append("SELECT t.").append(DataPermissionInterceptor.fields.get(type)).append(" FROM ");
                sql.append(DataPermissionInterceptor.tableName.get(type)).append(" t");
                sql.append(" WHERE t.role_id IN (");
                for (String role : roles) {
                    sql.append("'").append(role).append("',");
                }
                // 去除最后的逗号
                sql.deleteCharAt(sql.length() - 1);
                sql.append(")");
                if (type.equals("case")) {
                    sql.append(" union SELECT cr.case_id FROM base_case_user_relation cr WHERE cr.work_no = '" + workNo + "' ");
                }
                PreparedStatement resStmt2 = connection.prepareStatement(sql.toString());
                ResultSet rs2 = resStmt2.executeQuery();
                while (rs2.next()) {
                    res.add(rs2.getString(1));
                }
            }
        } catch (SQLException e) {
            LOGGER.error("生成获取权限语句异常sql ：" + sql.toString(), e);
        }
        return res;
    }

    private MappedStatement copyFromNewSql(MappedStatement ms, BoundSql boundSql, String sql,
                                           List<ParameterMapping> parameterMappings, Object parameter) {
        BoundSql newBoundSql = copyFromBoundSql(ms, boundSql, sql, parameterMappings, parameter);
        return copyFromMappedStatement(ms, new BoundSqlSqlSource(newBoundSql));
    }

    private BoundSql copyFromBoundSql(MappedStatement ms, BoundSql boundSql, String sql,
                                      List<ParameterMapping> parameterMappings, Object parameter) {
        BoundSql newBoundSql = new BoundSql(ms.getConfiguration(), sql, parameterMappings, parameter);
        for (ParameterMapping mapping : boundSql.getParameterMappings()) {
            String prop = mapping.getProperty();
            if (boundSql.hasAdditionalParameter(prop)) {
                newBoundSql.setAdditionalParameter(prop, boundSql.getAdditionalParameter(prop));
            }
        }
        return newBoundSql;
    }

    // see: MapperBuilderAssistant
    private MappedStatement copyFromMappedStatement(MappedStatement ms, SqlSource newSqlSource) {
        Builder builder = new Builder(ms.getConfiguration(), ms.getId(), newSqlSource, ms.getSqlCommandType());

        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        if (ms.getKeyProperties() != null && ms.getKeyProperties().length != 0) {
            StringBuffer keyProperties = new StringBuffer();
            for (String keyProperty : ms.getKeyProperties()) {
                keyProperties.append(keyProperty).append(",");
            }
            keyProperties.delete(keyProperties.length() - 1, keyProperties.length());
            builder.keyProperty(keyProperties.toString());
        }

        // setStatementTimeout()
        builder.timeout(ms.getTimeout());

        // setStatementResultMap()
        builder.parameterMap(ms.getParameterMap());

        // setStatementResultMap()
        builder.resultMaps(ms.getResultMaps());
        builder.resultSetType(ms.getResultSetType());

        // setStatementCache()
        builder.cache(ms.getCache());
        builder.flushCacheRequired(ms.isFlushCacheRequired());
        builder.useCache(ms.isUseCache());

        return builder.build();
    }
}
