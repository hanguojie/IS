<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="org.jasig.cas.client.validation.Assertion"%>
<%@page import="org.jasig.cas.client.util.AbstractCasFilter"%>
<%@page import="org.jasig.cas.client.authentication.AttributePrincipal"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'index.jsp' starting page</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script type="text/javascript" src="http://code.jquery.com/jquery-latest.js"></script>
	<script src="http://g.alicdn.com/dingding/dingtalk-pc-api/2.3.1/index.js"></script>

	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	
  </head>
  <script type="text/javascript">
  var defer = $.ajax({  
      url: 'http://172.16.11.58:8090/SF/' +'v1/sessions/getSign', 
      type: 'post',
      data: {'corpid': 'ding8e9d633d2aad4e4335c2f4657eb6378f'}         
    })
    
      defer.done(function(res){
            var _config = res.data;
            DingTalkPC.config({                                                          //实现验证
        		agentId : _config.agentId,
        		corpId : _config.corpId,
        		timeStamp : _config.timestamp,
        		nonceStr : _config.nonceStr,
        		signature : _config.signature,
        		jsApiList : [
        				'runtime.info',
        				'biz.contact.choose',
        				'device.notification.confirm', 
        				'device.notification.alert',
        				'device.notification.prompt',
        				'biz.ding.post',
        				'biz.util.openLink' ]
        	});
            //infoDefer.resolve(info);
          DingTalkPC.error(function(err) {  
              alert('DingTalkPC error: ' + JSON.stringify(err));  
          });  
          
        });
      
  

  DingTalkPC.ready(function() {                                               //验证成功

	  DingTalkPC.runtime.permission.requestAuthCode({                         //获取code码值
	corpId : 'ding8e9d633d2aad4e4335c2f4657eb6378f',
	onSuccess : function(info) {
		alert('authcode: ' + info.code);
		$.ajax({
			url : 'userinfo?code=' + info.code + '&corpid=ding8e9d633d2aad4e4335c2f4657eb6378f'     //请求后台通过code值获得userId
					,
			type : 'GET',
			success : function(data, status, xhr) {
				var info = JSON.parse(data);

				document.getElementById("userName").innerHTML = info.name;
				document.getElementById("userId").innerHTML = info.userid;

			},
			error : function(xhr, errorType, error) {
				logger.e("yinyien:" + _config.corpId);
				alert(errorType + ', ' + error);
			}
		});

	},
	onFail : function(err) {
		alert('fail: ' + JSON.stringify(err));
	}
});
});

  DingTalkPC.error(function(err) {                                             //验证失败
alert("进入到error中");
document.getElementById("userName").innerHTML = "验证出错";
alert('dd error: ' + JSON.stringify(err));
});

  </script>
  
  <body>
  	ff
  	
  	<a href="http://172.16.11.58:8080/TospurCAS2/logout">退出登录</a>
  </body>
</html>
