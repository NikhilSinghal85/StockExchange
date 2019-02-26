package com.example.config;

import java.util.ArrayList;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;



// This is the only required file with just 2 annotation @Configuration and @EnableSwagger2
// that's all this will give all API in out current directory exposed.
// other file are needed to further fine grain our swagger GUI.





@Configuration
@EnableSwagger2
public class SwaggerConfig {                                    
	private static final String BASE_URL = "/**";
	private static final String BASE_PACKAGE = "com.example";


	@Bean
	public Docket api() { 
		
		ResponseMessage one = new ResponseMessageBuilder().code(HttpStatus.OK.value()).message("Success").build();
		ResponseMessage one1 =new ResponseMessageBuilder().code(HttpStatus.INTERNAL_SERVER_ERROR.value()).message("Unexpected error").responseModel(new ModelRef("error")).build();
		ResponseMessage one2= new ResponseMessageBuilder().code(HttpStatus.NOT_FOUND.value()).message("Resource not found").build();
		ResponseMessage one3 =new ResponseMessageBuilder().code(HttpStatus.BAD_REQUEST.value()).message("Validation failed").responseModel(new ModelRef("error")).build();
		
		ArrayList<ResponseMessage> responseMessages = new ArrayList<ResponseMessage>();
		responseMessages.add(one);
		responseMessages.add(one1);
		responseMessages.add(one2);
		responseMessages.add(one3);
		
		return new Docket(DocumentationType.SWAGGER_2)  
				.select()                                  
				.apis(RequestHandlerSelectors.basePackage(BASE_PACKAGE))  
				.paths(PathSelectors.ant(BASE_URL))                          
				.build()
				//.apiInfo(apiInfo())
				.useDefaultResponseMessages(false)                                   
				.globalResponseMessage(RequestMethod.GET, responseMessages)
				.globalResponseMessage(RequestMethod.PUT, responseMessages)
				.globalResponseMessage(RequestMethod.POST, responseMessages);

	}
}