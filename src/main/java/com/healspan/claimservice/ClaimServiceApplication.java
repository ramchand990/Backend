package com.healspan.claimservice;

import com.healspan.claimservice.mstupload.claim.exception.RestTemplateResponseErrorHandler;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class ClaimServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClaimServiceApplication.class, args);
	}
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder, RestTemplateResponseErrorHandler restTemplateResponseErrorHandler) {
		return builder.errorHandler(restTemplateResponseErrorHandler).build();
	}

}
