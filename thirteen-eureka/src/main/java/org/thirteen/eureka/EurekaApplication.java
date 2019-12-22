package org.thirteen.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.annotation.Configuration;

/**
 * @author Aaron.Sun
 * @description Eureka服务注册中心启动
 * <p>
 * * @SpringBootApplication 等同于@Configuration　+　@EnableAutoConfiguration和 　+　@ComponentScan
 * * @SpringBootApplication注解自动获取应用的配置信息，会给应用带来一些副作用。
 * 由自动配置（ auto-configuration ）和 组件扫描 （ component scanning ）组成，
 * 这跟使用 @Configuration、@EnableAutoConfiguration 和 @ComponentScan 三个注解的作用是一样的。
 * 这样做给开发带来方便的同时，也会有三方面的影响：
 * <p>
 * 1、会导致项目启动时间变长。当启动一个大的应用程序,或将做大量的集成测试启动应用程序时，影响会特别明显。
 * 2、会加载一些不需要的多余的实例（beans）。
 * 3、会增加 CPU 消耗。
 * <p>
 * 针对以上三个情况，我们可以移除 @SpringBootApplication 然后使用 @Configuration、@EnableAutoConfiguration 和 @ComponentScan注解来扫描特定的包
 * @date Created in 14:35 2018/4/7
 * @modified by
 */
@Configuration
@EnableAutoConfiguration
@EnableEurekaServer
public class EurekaApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaApplication.class, args);
    }

}
