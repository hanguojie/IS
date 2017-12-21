package com.tospur.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.RequestMethod;

import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Import({ springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration.class })
@EnableSwagger2
public class SwaggerConfig {
	@Bean
	public Docket customImplementation() {
		final ResponseMessage resp500 =  new ResponseMessageBuilder()   
	    .code(500)
	    .message("内部错误")
	    .responseModel(new ModelRef("Error"))
	    .build();
		final ResponseMessage resp404 = new ResponseMessageBuilder() 
		      .code(404)
				      .message("路径错误")
				    .responseModel(new ModelRef("Error"))
			      .build();
		final ResponseMessage resp400 = new ResponseMessageBuilder() 
			      .code(400)
					      .message("请求错误，请求或参数格式错误")
					    .responseModel(new ModelRef("Error"))
				      .build();			
		final List<ResponseMessage> responseMessages = new ArrayList<>();
		responseMessages.add(resp400);
		responseMessages.add(resp404);
		responseMessages.add(resp500);
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).useDefaultResponseMessages(false).enable(true)
				.globalResponseMessage(RequestMethod.GET, responseMessages)
				.globalResponseMessage(RequestMethod.POST, responseMessages)
				.globalResponseMessage(RequestMethod.PUT, responseMessages)
				.globalResponseMessage(RequestMethod.DELETE, responseMessages);
	}

	private ApiInfo apiInfo() {
		  return new ApiInfo(
			      "同策数据API",
			      "了解API，了解数据，了解同策。",
			      "v1",
			      null,
			      new Contact("同策", "http://www.tospur.com", "it-support@tospur.com"),
			      null,
			      null);
	}

}