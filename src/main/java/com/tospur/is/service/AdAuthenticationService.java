package com.tospur.is.service;

import java.util.Date;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tospur.is.entity.LoginInfo;
import com.tospur.utils.DateUtil;
import com.tospur.utils.Encrypt;
import com.tospur.utils.GlobalParameter;
import com.tospur.utils.HttpRequestTools;

/**
 * 去 ActiveDirectory 认证用户
 *
 * @author XXZ
 */
@Service
public class AdAuthenticationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdAuthenticationService.class);
    //ad域账户验证接口
    private static final String adCheckUrl = "%s/ldap_ad/check_user.htm?loginId=%s&password=%s&echostr=%s&token=%s";
    
    private static final String resetPasswordUrl = "%s/ldap_ad/reset_pass.htm?loginId=%s&newPassword=%s&echostr=%s&token=%s";
    
    /**
     * Active Directory 认证服务
     *
     * @param logintoken 登录信息
     * @return Ad认证是否通过
     */
    public boolean login(LoginInfo logintoken) {
//    	return true;
        boolean flag = false;
        final String userName = logintoken.getUserName();
        final String password = logintoken.getPassword();
        // TODO 预留写死admin账户信息 后期更改
        //预留账号8做测试用
        if (userName.equals("36199")&& password.equals("admin")) {
            flag = true;
        } else {
            //ad域账户验证
            flag = checkUser(userName, password);
        }
        return flag;
    }

    /**
     * 验证ad域账号密码
     * 验证规则详见《同策数据平台接口文档》
     *
     * @param userName 账号
     * @param password 密码
     * @return
     */
    public boolean checkUser(String userName, String password) {
        String echostr = UUID.randomUUID().toString();
        String token = Encrypt.sha(GlobalParameter.adSign + echostr + DateUtil.dateToStringToIos(new Date()));
        String url = String.format(adCheckUrl, GlobalParameter.adIp, userName, Encrypt.getBase64(password), echostr, token);
        String jsonStr = HttpRequestTools.httpRequest(url);
        JSONObject jsonObject = JSONArray.parseObject(jsonStr);
        if (jsonObject != null && jsonObject.get("errcode") != null) {
            if (jsonObject.get("errcode").toString().equals("10000")) {
                return true;
            } else {
                LOGGER.error(jsonObject.get("errmsg").toString());
            }
        }
        return false;
    }
    
    
    
}
