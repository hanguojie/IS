package com.tospur.is.controller;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.annotation.Resource;
import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tospur.is.entity.LoginInfo;
import com.tospur.is.service.AdAuthenticationService;
import com.tospur.is.service.OAUserService;
import com.tospur.is.service.SessionService;
import com.tospur.is.vo.LoginResultUserVo;
import com.tospur.utils.response.Resp;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;

@CrossOrigin
@RestController
@RequestMapping("/v1/sessions")
@Api(tags = "IS-会话")
public class LoginController {
	private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);
	
	@Resource 
	// 会话服务(TOKEN及用户登录状态管理)
	private SessionService sessionService;
	
	
	@Resource
	// 登录认证
	private AdAuthenticationService adAuthenticationService; 
	
	@Resource
	//OA获取数据
	private OAUserService oaUserService;
	
	
	
	@ApiOperation(value = "PC端登录", httpMethod = "POST", notes = "PC端登录")
	@RequestMapping(value = "/pcLogin", method = RequestMethod.POST)
	public Resp pcLogin(HttpServletRequest request, @RequestBody LoginInfo loginInfo) {
		LoginResultUserVo loginResult = adAuthentication2(loginInfo);
		if (loginResult != null) {
			
			Resp resp = Resp.success();
			resp.setData(loginResult);
			return resp;
		} else {
			return Resp.failed("帐号或密码错误");
		}
		
	}
	
	@ApiOperation(value = "删除会话，用户注销",
	          httpMethod = "DELETE",
	          response = ApiResponse.class,
	          notes = "根据会话ID删除会话，即用户注销")
	  @RequestMapping(value = "logout/{token}", method = RequestMethod.DELETE)
	  public Resp logout(
			  @ApiParam(required = true, name = "token", value = "会话ID") 
			  @PathVariable String token) {
		try {
			final boolean validate = sessionService.isTokenValid(token);
			if(!validate){
				LOGGER.error("token is invalidate, token={}", token);
				return Resp.failed("logout failed");
			}
			sessionService.destroyToken(token);
			return Resp.success();
		} catch (Exception e) {
			String respMsg = "logout failed";
			LOGGER.error(respMsg, e);
			return Resp.failed(respMsg);
		}
	}
	
	// 去 AD 认证
	private LoginResultUserVo adAuthentication(LoginInfo loginInfo) {
		if(adAuthenticationService.login(loginInfo)){
			
			String token = sessionService.GenAndSaveToken(loginInfo.getUserName());
			Map<String, String> userInfo=oaUserService.getUserInfoById(loginInfo.getUserName());
			final LoginResultUserVo loginResult = new LoginResultUserVo();
			loginResult.setToken(token);
			loginResult.setUserId(loginInfo.getUserName());
			if(userInfo!=null){
				loginResult.setPhone(userInfo.get("phone"));
				loginResult.setUserName(userInfo.get("name"));
				loginResult.setDepartment(userInfo.get("department"));
			}
			return loginResult;
		}
		return null;
	}
	
	private LoginResultUserVo adAuthentication2(LoginInfo loginInfo) {
		if(!check(loginInfo.getUserName(),loginInfo.getPassword()).isEmpty()){
			
			String token = sessionService.GenAndSaveToken(loginInfo.getUserName());
			Map<String, String> userInfo=oaUserService.getUserInfoById(loginInfo.getUserName());
			final LoginResultUserVo loginResult = new LoginResultUserVo();
			loginResult.setToken(token);
			loginResult.setUserId(loginInfo.getUserName());
			if(userInfo!=null){
				loginResult.setPhone(userInfo.get("phone"));
				loginResult.setUserName(userInfo.get("name"));
				loginResult.setDepartment(userInfo.get("department"));
			}
			return loginResult;
		}
		return null;
	}
	
	public static Map<String, String> check(String workNo,String name){
		Map<String, String> userInfo=new HashMap<>();
		String host = "172.30.100.3";//AD域IP，必须填写正确
        String domain = "@tospur.com";//域名后缀，例.@noker.cn.com
        String port = "389"; //端口，一般默认389
        String url = new String("ldap://" + host + ":" + port);//固定写法
//        String user = userName.indexOf(domain) > 0 ? userName : userName
//                + domain;//网上有别的方法，但是在我这儿都不好使，建议这么使用
        Hashtable<String, String> env = new Hashtable<String,String>();//实例化一个Env
        env.put(Context.SECURITY_AUTHENTICATION, "simple");//LDAP访问安全级别(none,simple,strong),一种模式，这么写就行
        env.put(Context.SECURITY_PRINCIPAL, "it-support@tospur.com"); //用户名
        env.put(Context.SECURITY_CREDENTIALS, "tospur&2017");//密码
        env.put(Context.INITIAL_CONTEXT_FACTORY,
                "com.sun.jndi.ldap.LdapCtxFactory");// LDAP工厂类
        env.put(Context.PROVIDER_URL, url);//Url
        LdapContext ctx=null;
        try {
        	ctx = new InitialLdapContext(env,null);// 初始化上下文
            SearchControls searchCtls = new SearchControls();
			searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
			String searchFilter = "(&(employeeid="+workNo+")(sn="+name+"))";
			String searchBase = "ou=Tospur_OU,DC=tospur,DC=com";
			String returnedAtts[] = {"sn", "userPrincipalName", "mobile"};  
			searchCtls.setReturningAttributes(returnedAtts);
			NamingEnumeration<SearchResult> answer = ctx.search(searchBase, searchFilter,searchCtls);
			while (answer.hasMoreElements()) {
				SearchResult sr = (SearchResult) answer.next();
//				System.out.println("<<<::[" + sr.getName()+"]::>>>>");
//				System.out.println(answer.next().toString());
				String dn = sr.getName();  
			    Attributes Attrs = sr.getAttributes();// 得到符合条件的属性集  
			    if (Attrs != null) {  
				     try {  
		//				      for (NamingEnumeration ne = Attrs.getAll(); ne.hasMore();) {  
		//				       Attribute Attr = (Attribute) ne.next();// 得到下一个属性  
		//				       System.out.println(" AttributeID=属性名："+ Attr.getID().toString());  
		//				       // 读取属性值  
		//				       for (NamingEnumeration e = Attr.getAll(); e.hasMore(); totalResults++) {  
		//				      company =  e.next().toString();  
		//				        System.out.println("    AttributeValues=属性值：" + company);  
		//				       }  
		//				       System.out.println("    ---------------");  
		//				         
		//				      }
//						 System.out.println(Attrs.get("mobile").get());  
//						 System.out.println(Attrs.get("userPrincipalName").get());  
//						 System.out.println(Attrs.get("sn").get());  
//						 userInfo.put("mobile", Attrs.get("mobile").get().toString());
//						 userInfo.put("userPrincipalName", Attrs.get("userPrincipalName").get().toString());
						 userInfo.put("sn", Attrs.get("sn").get().toString());
				     } catch (NamingException e) {  
				      System.err.println("Throw Exception : " + e);  
				     }  
			    }else{
			    	return null;
			    }

			}
			ctx.close();

            return userInfo;
        } catch (AuthenticationException e) {
        	LOGGER.error("身份验证失败!url="+url,e);
            return null;
        } catch (javax.naming.CommunicationException e) {
        	LOGGER.error("AD域连接失败!,url="+url,e);
            return null;
        } catch (Exception e) {
        	LOGGER.error("身份验证未知异常!,url="+url,e);
            return null;
        } finally{
            if(null!=ctx){
                try {
                    ctx.close();
                    ctx=null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
		
	}
		
}
