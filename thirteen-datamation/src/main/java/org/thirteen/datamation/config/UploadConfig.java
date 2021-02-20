package org.thirteen.datamation.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

import javax.servlet.MultipartConfigElement;

/**
 * @author Aaron.Sun
 * @description 文件上传配置
 * @date Created in 13:06 2018/4/15
 * @modified by
 */
@Configuration
public class UploadConfig {

    @Value("${upload.tmp}")
    private String uploadTmp;
    @Value("${upload.path}")
    private String uploadPath;

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        // 设置文件上传临时路径
        factory.setLocation(uploadTmp);
        // 设置最大上传文件大小
        factory.setMaxFileSize(DataSize.ofBytes(1024L * 1024L));
        return factory.createMultipartConfig();
    }

}
