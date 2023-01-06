package org.thirteen.workstation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author Aaron.Sun
 * @description 工作站模块启动类
 * @date Created in 18:36 2022/11/9
 * @modified By
 */
@SpringBootApplication
@EnableDiscoveryClient
public class WorkstationApplication {

    public static void main(String[] args) {
        SpringApplication.run(WorkstationApplication.class, args);
    }

}
