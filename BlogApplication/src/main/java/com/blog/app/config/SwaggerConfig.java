package com.blog.app.config;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {

	public static final String AUTHORIZATION_HEADER = "Authorization";

	private List<SecurityContext> securityContext() {

		return Arrays.asList(SecurityContext.builder().securityReferences(securityRef()).build());

	}

	private List<SecurityReference> securityRef() {

		AuthorizationScope as = new AuthorizationScope("global", "access everything");

		return Arrays.asList(new SecurityReference("JWT", new AuthorizationScope[] { as }));
	}

	private ApiKey apiKey() {
		return new ApiKey("JWT", AUTHORIZATION_HEADER, "header");
	}

	@Bean
	Docket docketBean() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(getInfo()).securityContexts(securityContext())
				.securitySchemes(Arrays.asList(apiKey())).select().apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any()).build();
	}

	private ApiInfo getInfo() {

		return new ApiInfo("Blogging Application", "This Project is Developed By Mr Akshay Malapure", "1.0",
				"Terms of Service", new Contact("Akshay", null, "akshaymalapure1@gmail.com"), "License of Apis",
				"ApiLicenseUrl", Collections.emptyList());

	}

}
