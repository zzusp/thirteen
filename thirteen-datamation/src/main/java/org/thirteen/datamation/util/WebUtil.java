package org.thirteen.datamation.util;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Aaron.Sun
 * @description web工具类
 * @date Created in 2019/2/21 14:57
 * @modified by
 */
public class WebUtil {

    private static final String UNKNOWN = "unknown";

    /**
     * 由请求对象获取请求请求来源的ip地址
     *
     * @param request 请求对象
     * @return 请求来源的ip地址
     */
    public static String getIpAddr(HttpServletRequest request) {
        // 先从nginx自定义配置获取，需要nginx配置 proxy_set_header X-real-ip $remote_addr;
        String ip = request.getHeader("X-real-ip");
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("x-forwarded-for");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

}
