package org.thirteen.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * @author Aaron.Sun
 * @description 分布式配置中心服务启动
 * @date Created in 14:35 2018/4/7
 * @modified by
 * {@SpringBootApplication} 等同于@Configuration　+　@EnableAutoConfiguration和 　+　@ComponentScan
 */
@SpringBootApplication
@EnableConfigServer
public class ConfigApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfigApplication.class, args);
    }

}
