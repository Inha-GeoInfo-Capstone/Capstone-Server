package com.inha.capstone.capstone.config;

import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLException;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient() throws SSLException {
        SslContextBuilder sslContextBuilder = SslContextBuilder
                .forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE);

        HttpClient httpClient = HttpClient.create()
                .secure(t -> t.sslContext(sslContextBuilder));

        // WebClient Bean 생성
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .baseUrl("https://127.0.0.1")  // Flask HTTPS 주소
                .build();
    }
}
