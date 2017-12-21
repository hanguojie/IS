package com.tospur.is.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.tospur.is.vo.DingTalkHrmResourceVo;

@Repository()
public class OAUserDao {
	private static final Logger LOGGER = LoggerFactory.getLogger(OAUserDao.class);
	
	@Resource(name = "oaDataSource")
    private DataSource dataSource;
	
	public Map<String, String> getUserInfoById(String workNo){
		long startTime = System.currentTimeMillis();
		LOGGER.info("StartTime:"+startTime);
		
		String querySql="select hrmname,hrmno,email,departmentname,tel from view_hrmresouce where hrmno=? and status='0'";
		LOGGER.info("OA用户查询SQL-->" + querySql);
		LOGGER.info("OA用户查询parm-->" + workNo);
		
		Connection connection = null;
        PreparedStatement ps=null;
        ResultSet rs = null;
		
		try {
			connection = dataSource.getConnection();
			ps=connection.prepareStatement(querySql);
			ps.setString(1, workNo);
			rs=ps.executeQuery();
			if (rs.next()) { 
				 Map<String, String> dataMap = new HashMap<String, String>();
				 dataMap.put("name", rs.getString("hrmname"));
				 dataMap.put("workNo", rs.getString("hrmNo"));
				 dataMap.put("email", rs.getString("email"));
				 dataMap.put("department", rs.getString("departmentname"));
				 dataMap.put("phone", rs.getString("tel"));
				 return dataMap;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			if(rs!=null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(ps!=null){
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(connection!=null){
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	public List<DingTalkHrmResourceVo> getUserInfo(){
		long startTime = System.currentTimeMillis();
		LOGGER.info("StartTime:"+startTime);
		
		String querySql="SELECT id,id userid,lastname NAME,1 showorder,1 dingtalkId,lastname jobname,mobile,email,workcode workcode from hrmresource where workcode in (?,?,?)";
		LOGGER.info("OA用户查询SQL-->" + querySql);
		
		Connection connection = null;
        PreparedStatement ps=null;
        ResultSet rs = null;
		
		try {
			connection = dataSource.getConnection();
			ps=connection.prepareStatement(querySql);
			ps.setString(1, "38670");
			ps.setString(2, "35718");
			ps.setString(3, "37132");
			rs=ps.executeQuery();
			List<DingTalkHrmResourceVo> list=new ArrayList<>();
			while (rs.next()) { 
				 DingTalkHrmResourceVo vo=new DingTalkHrmResourceVo();
				 vo.setUserId(rs.getString("userid"));
				 vo.setId(rs.getLong("id"));
				 vo.setName(rs.getString("name"));
				 vo.setShowOrder(rs.getInt("showorder"));
				 vo.setDingtalkId(rs.getLong("dingtalkid"));
				 vo.setJobName(rs.getString("jobname"));
				 vo.setMobile(rs.getString("mobile"));
				 vo.setWorkcode(rs.getString("workcode"));
				 list.add(vo);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			if(rs!=null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(ps!=null){
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(connection!=null){
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	public String getUserIdByWorkNo(String workNo){
		long startTime = System.currentTimeMillis();
		LOGGER.info("StartTime:"+startTime);
		
		String querySql="select id from hrmresource where workcode=?";
		LOGGER.info("OA用户查询SQL-->" + querySql);
		LOGGER.info("OA用户查询parm-->" + workNo);
		
		Connection connection = null;
        PreparedStatement ps=null;
        ResultSet rs = null;
		
		try {
			connection = dataSource.getConnection();
			ps=connection.prepareStatement(querySql);
			ps.setString(1, workNo);
			rs=ps.executeQuery();
			if (rs.next()) { 
				 return rs.getString("id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			if(rs!=null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(ps!=null){
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(connection!=null){
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
}
