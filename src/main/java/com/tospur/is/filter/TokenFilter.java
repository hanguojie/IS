package com.tospur.is.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.context.WebApplicationContext;

import com.alibaba.fastjson.JSONObject;
import com.tospur.utils.response.Resp;
import com.tospur.utils.response.RespStatus;

public class TokenFilter implements Filter{
	public static String[] urlM;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TokenFilter.class);
	
	
//	@Autowired
//	private SessionService sessionService;
	
	//private SessionService sessionService;
	private RedisTemplate<String,String> redisTemplate;
	
	@Override
	public void destroy() {
		System.out.println("in filter destroy");
	}

	@Override 
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpresponse = (HttpServletResponse) response;
		
		//开始时间
		long startTime = System.currentTimeMillis();
		
		String method = httpRequest.getMethod();
		if(method != null && method.equals("OPTIONS")){
			chain.doFilter(request, response);	//放行
			long endTime = System.currentTimeMillis();
			LOGGER.info("{----------------method="+httpRequest.getRequestURI()+",time="+(endTime-startTime)+"ms }");
			return;
		}
		if("/IS/api/v1/process/uploadImage".equals(httpRequest.getRequestURI())){
			chain.doFilter(request, response);	//放行
			long endTime = System.currentTimeMillis();
			LOGGER.info("{----------------method="+httpRequest.getRequestURI()+",time="+(endTime-startTime)+"ms }");
			return;
		}
		if("/IS/api/v1/process/getPInfoByType".equals(httpRequest.getRequestURI())){
			chain.doFilter(request, response);	//放行
			long endTime = System.currentTimeMillis();
			LOGGER.info("{----------------method="+httpRequest.getRequestURI()+",time="+(endTime-startTime)+"ms }");
			return;
		}
		if("/IS/api/v1/process/findPListByType".equals(httpRequest.getRequestURI())){
			chain.doFilter(request, response);	//放行
			long endTime = System.currentTimeMillis();
			LOGGER.info("{----------------method="+httpRequest.getRequestURI()+",time="+(endTime-startTime)+"ms }");
			return;
		}
		if("/IS/api/v1/process/findRankingByType".equals(httpRequest.getRequestURI())){
			chain.doFilter(request, response);	//放行
			long endTime = System.currentTimeMillis();
			LOGGER.info("{----------------method="+httpRequest.getRequestURI()+",time="+(endTime-startTime)+"ms }");
			return;
		}
		if("/IS/api/v1/process/findAddList".equals(httpRequest.getRequestURI())){
			chain.doFilter(request, response);	//放行
			long endTime = System.currentTimeMillis();
			LOGGER.info("{----------------method="+httpRequest.getRequestURI()+",time="+(endTime-startTime)+"ms }");
			return;
		}
		if("/IS/api/v1/process/findAllList".equals(httpRequest.getRequestURI())){
			chain.doFilter(request, response);	//放行
			long endTime = System.currentTimeMillis();
			LOGGER.info("{----------------method="+httpRequest.getRequestURI()+",time="+(endTime-startTime)+"ms }");
			return;
		}
		String token = httpRequest.getHeader("X-Token");
		
		if (StringUtils.isNotEmpty(token)) {
			boolean exists = (boolean) redisTemplate.execute((RedisCallback) connection -> connection.exists(token.getBytes()));
			if (exists) {
				chain.doFilter(request, response);	//放行
				long endTime = System.currentTimeMillis();
				LOGGER.info("{----------------method="+httpRequest.getRequestURI()+",time="+(endTime-startTime)+" ms }");
				return;
			}	
		}
		httpresponse.setStatus(HttpServletResponse.SC_OK);
		httpresponse.setContentType("application/json;charset=UTF-8");
		httpresponse.setHeader("Access-Control-Allow-Origin", "*");
		// 无效token的错误码为401
//		httpRequest.get
		
		Resp resp = new Resp();
		resp.setStatus(new RespStatus("401", "未授权的登录"));
		JSONObject object = (JSONObject) JSONObject.toJSON(resp);
		httpresponse.getWriter().write(object.toString());
		return;
	}
	
	
	//@Override
	public void doFilter2(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpresponse = (HttpServletResponse) response;
		httpresponse.setCharacterEncoding("UTF-8");
		httpresponse.setHeader("Content-type", "application/json;charset=UTF-8");  
		 
		System.out.println("判断url start");
		String url = httpRequest.getRequestURL().toString();
		String token = httpRequest.getHeader("token");
		String method = httpRequest.getMethod();
//		System.out.println(httpRequest.getHeader("Cache-Control"));//token
		System.out.println(method);
		boolean flag = false;
		 for (int i = 0; i < urlM.length; i++) {
				String [] urls = urlM[i].split("-");
				System.out.println(urls[0]+"  "+urls[1]);
				//如果是这种url加请求类型，不验证
				if(url.contains(urls[0]) && urls[1].equals(method)){
					System.out.println("这样的不拦截");
					flag = true;
					break;
				}else{
					System.out.println("啊哦,被拦截了");
					 //token校验
					if(!"admin#2016428165822".equals(token)){
						
						flag = false;
					}
				}
			}
		if(!flag){
			Resp resp = Resp.failed();
			resp.setData("安全校验不通过");
			JSONObject object = (JSONObject) JSONObject.toJSON(resp);
			httpresponse.getWriter().write(object.toString());
			return;
		}
//		if(url.contains("departments") && method.equals("POST") && token.equals("qwq")){
			//返回response;
//			Resp resp = Resp.failed();
//			resp.setData("安全校验不通过");
//			HttpServletResponse  responses = (HttpServletResponse) response;
//			response.getWriter().write("安全校验不通过--------");
			
//			chain.doFilter(request, response);
//		}
		chain.doFilter(request, response);
		
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
		ServletContext sc = filterConfig.getServletContext();
	    WebApplicationContext wac = (WebApplicationContext) sc.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
	    //获得WebApplicationContext还可以调用
	    //WebApplicationContextUtils.getWebApplicationContext(sc);
	    //当然最后Spring都是调用getAttribute方法.
		System.out.println("初始化");
	    redisTemplate = (RedisTemplate<String,String>)wac.getBean("redisTemplate");

		
	}
}
