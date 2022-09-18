package com.blog.app.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

	@Bean
	public ModelMapper getModelMapperBean() {
		return new ModelMapper();
	}
}
