package com.inha.capstone.capstone.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Inha Campus Navigation Swagger")
                        .description("인하대학교 캠퍼스 내 네비게이션 기능 제공 API 명세서")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("인하대 공간정보 캡스톤")
                                .email("tmddls91@naver.com")
                        )
                );
    }
}