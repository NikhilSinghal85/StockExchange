package com.example.config;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;



// This is the only required file with just 2 annotation @Configuration and @EnableSwagger2
// that's all this will give all API in out current directory exposed.
// other file are needed to further fine grain our swagger GUI.


@Configuration
@EnableSwagger2
public class SwaggerConfig {

	
	// below all are only to display in swagger GUI we can skip these too.
	
  public static final Contact DEFAULT_CONTACT = new Contact(
      "Nikhil", "http://www.xyz.com", "nikhil@gmail.com");
  
  public static final ApiInfo DEFAULT_API_INFO = new ApiInfo(
      "Available Stock Exchange API's", "These API are available to user to fetch different available Stock for different available options", "1.0",
      "urn:tos", DEFAULT_CONTACT, 
      "Apache 2.0", "http://www.apache.org/licenses/LICENSE-2.0");

  
  // this populates the Response content type in swagger GUI.
  private static final Set<String> DEFAULT_PRODUCES_AND_CONSUMES = 
      new HashSet<String>(Arrays.asList("application/json",
          "application/xml"));

  @Bean
  public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2)
        .apiInfo(DEFAULT_API_INFO)
        .produces(DEFAULT_PRODUCES_AND_CONSUMES)
        .consumes(DEFAULT_PRODUCES_AND_CONSUMES);
  }
}