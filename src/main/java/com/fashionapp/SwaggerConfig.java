package com.fashionapp;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
//@EnableAutoConfiguration

public class SwaggerConfig {
	
    public Docket api(){
        return new Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.fashionapp.Controller"))
           // .apis(RequestHandlerSelectors.any())
            .paths(PathSelectors.any())
            .build()
            .apiInfo(apiInfo());
    }

	    private ApiInfo apiInfo() {
	        return new ApiInfoBuilder()
	            .title("TITLE")
	            .description("DESCRIPTION")
	            .version("VERSION")
	            .termsOfServiceUrl("http://terms-of-services.url")
	            .license("LICENSE")
	            .licenseUrl("http://url-to-license.com")
	            .build();
	    }
	    
}