package org.thirteen.gateway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author Aaron.Sun
 * @description Zipkin测试接口
 * @date Created in 16:04 2020/7/14
 * @modified By
 */
@RestController
public class TestZipkin {

    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/testZipKin")
    public String testZipKin() {
        ResponseEntity<String> res = restTemplate.getForEntity("http://122.152.221.117:8769/api-authorization/login",
            String.class, "test", "1234");
        return res.getBody();
    }
}
