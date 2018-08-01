package com.phr.tx.service.message.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	@Bean
	public Docket createRestApi() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select()
				.apis(RequestHandlerSelectors.basePackage("com.phr.tx.service.message.*.controller")).paths(PathSelectors.any()).build();
	}

	private ApiInfo apiInfo() {
		/*ApiInfo apiInfo = new ApiInfo("额度系统对外提供api", "", "1.0",
				"Terms of service", new Contact("额度重构小组", "", ""), "", "");*/
		return new ApiInfoBuilder()
                //页面标题
                .title("Spring Boot 测试使用 Swagger2 构建RESTful API")
                .termsOfServiceUrl("")
                //创建人
                .contact("phr")
                //版本号
                .version("1.0")
                //描述
                .description("API 描述")
                .build();
	}
}
