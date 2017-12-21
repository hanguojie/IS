package com.tospur.is.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * swagger拦截
 * 配置文件配置swagger是否显示
 * 测试环境swagger.hide配置为true为显示
 * 生产环境swagger.hide配置为false为隐藏
 * @author oyj
 *
 */
public class SwaggerRoute extends HandlerInterceptorAdapter{

	@Value("${swagger.hide}")
	private String swagger;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// TODO Auto-generated method stub
		if(!"true".equalsIgnoreCase(swagger)){
			String url=request.getRequestURL().toString();
			if(StringUtils.isNotBlank(url)&&url.contains("swagger")){
				System.out.println(request.getRequestURL().toString());
				return false;
			}
		}
		return true;
	}

}
