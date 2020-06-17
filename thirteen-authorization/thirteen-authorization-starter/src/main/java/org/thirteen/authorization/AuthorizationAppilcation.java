package org.thirteen.authorization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author Aaron.Sun
 * @description 鉴权授权模块启动类
 * @date Created in 16:50 2019/12/15
 * @modified By
 */
@SpringBootApplication
@EnableDiscoveryClient
public class AuthorizationAppilcation {

    public static void main(String[] args) {
        SpringApplication.run(AuthorizationAppilcation.class, args);
    }

}
