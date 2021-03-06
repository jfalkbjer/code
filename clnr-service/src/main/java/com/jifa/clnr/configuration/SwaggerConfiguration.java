package com.jifa.clnr.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@PropertySource("classpath:swagger.properties")
@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.jifa.clnr.rest")).paths(PathSelectors.any()).build()
				.apiInfo(apiInfo()).host("falkbjer.asuscomm.com");
	}

	private ApiInfo apiInfo() {
		return new ApiInfo("Clearing Number API", "API to get bank name for a clearing number.", "1.0",
				"Terms of service", new Contact("jfalkbjer", "", ""), "Apache 2.0",
				"http://www.apache.org/licenses/LICENSE-2.0");
	}
}
