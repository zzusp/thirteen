package org.thirteen.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

/**
 * @author Aaron.Sun
 * @description 网关路由模块启动类
 * @date Created in 22:09 2020/6/23
 * @modified By
 */
@SpringBootApplication
public class GatewayAppilcation {

    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/testZipKin")
    public String addToCart() {
        ResponseEntity<String> res = restTemplate.getForEntity("http://122.152.221.117:8769/api-authorization/login",
            String.class, "test", "1234");
        return res.getBody();
    }

    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory factory) {
        return new RestTemplate(factory);
    }

    @Bean
    public ClientHttpRequestFactory clientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000);
        factory.setReadTimeout(5000);
        return factory;
    }

    public static void main(String[] args) {
        SpringApplication.run(GatewayAppilcation.class, args);
    }



}
