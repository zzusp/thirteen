package org.thirteen.zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Aaron.Sun
 * @description 简单拦截器，可改写为权限验证
 * @date Created in 21:43 2018/4/9
 * @modified by
 */
@Component
public class PreFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        HttpServletResponse response = ctx.getResponse();
        Cookie[] cookies = request.getCookies();
        // 遍历cookie获取sessionId
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                System.out.println(cookie.getDomain());
                System.out.println(cookie.getPath());
                System.out.println(cookie.getValue());
            }
        }
        return null;
    }

}
