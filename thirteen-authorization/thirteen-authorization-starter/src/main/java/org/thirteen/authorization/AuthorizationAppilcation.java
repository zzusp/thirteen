package org.thirteen.authorization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Aaron.Sun
 * @description 鉴权授权模块启动类
 * @date Created in 16:50 2019/12/15
 * @modified By
 */
@SpringBootApplication
@EnableEurekaClient
@EnableSwagger2
@EnableJpaRepositories(basePackages = "org.thirteen.authorization.repository")
public class AuthorizationAppilcation {

    public static void main(String[] args) {
        SpringApplication.run(AuthorizationAppilcation.class, args);
    }

}
