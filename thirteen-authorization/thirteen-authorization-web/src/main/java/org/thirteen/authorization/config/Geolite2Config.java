package org.thirteen.authorization.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author Aaron.Sun
 * @description geolite2数据库配置
 * @date Created in 18:21 2019/12/15
 * @modified By
 */
@Configuration
public class Geolite2Config {

    @Value("${geolite2.city.db.path}")
    private String geolite2CityDbPath;

    /**
     * 初始化geolite2数据库读取对象
     */
/*    @Bean
    public DatabaseReader geolite2CityDb() throws Exception {
        return new DatabaseReader.Builder(new File(geolite2CityDbPath)).build();
    }*/
}
