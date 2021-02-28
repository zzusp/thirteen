package org.thirteen.datamation.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.security.SignatureException;
import java.util.Date;

/**
 * @author Aaron.Sun
 * @description jwt工具类
 * @date Created in 15:26 2019/12/15
 * @modified By
 */
public class JwtUtil {
    /**
     * 签发者
     */
    private static final String ISSUER = "thrirteen.auth";
    /**
     * 原密钥
     */
    private static final String SECRET = "thrirteen.jwt.secret";
    /**
     * 默认过期时间30分钟
     */
    private static final long EXPIRE_TIME = 30 * 60 * 1000L;
    /**
     * local线程，用来存储获取当前登录用户
     */
    private static ThreadLocal<String> threadLocal = new ThreadLocal<>();

    /**
     * 生成JWT token，默认过期时间30分钟
     *
     * @param subject 面向用户（可用用户账号）
     * @return token
     */
    public static String sign(String subject) {
        return sign(subject, EXPIRE_TIME);
    }

    /**
     * 生成JWT token
     *
     * @param subject 面向用户（可用用户账号）
     * @param expire  过期时间，毫秒
     * @return token
     */
    public static String sign(String subject, long expire) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        if (expire <= 0) {
            expire = EXPIRE_TIME;
        }
        Date exp = new Date(nowMillis + expire);
        return Jwts.builder()
            // 设置签发时间
            .setIssuedAt(now)
            // 设置签发者
            .setIssuer(ISSUER)
            // 设置面向用户
            .setSubject(subject)
            // 设置加密算法和密钥
            .signWith(SignatureAlgorithm.HS256, generalKey())
            // 设置过期时间
            .setExpiration(exp)
            .compact();
    }

    /**
     * 验证JWT token，并返回解析后的token对象
     * 验证失败时会抛出以下异常
     * ExpiredJwtException 过期异常
     *
     * @param token token
     * @return 解析后的token对象
     */
    public static Jws<Claims> verify(String token) {
        return Jwts.parser().setSigningKey(generalKey()).parseClaimsJws(token);
    }

    /**
     * 获取token中注册信息
     *
     * @param token token
     * @return token中注册信息
     */
    public static Claims getTokenClaim(String token) {
        return verify(token).getBody();
    }

    /**
     * 获取token失效时间
     *
     * @param token token
     * @return token失效时间
     */
    public static Date getExpirationDateFromToken(String token) {
        return getTokenClaim(token).getExpiration();
    }

    /**
     * 获取用户名从token中
     *
     * @param token token
     * @return 用户名
     */
    public static String getSubjectFromToken(String token) {
        return getTokenClaim(token).getSubject();
    }

    /**
     * 获取jwt发布时间
     *
     * @param token token
     * @return jwt发布时间
     */
    public static Date getIssuedAtDateFromToken(String token) {
        return getTokenClaim(token).getIssuedAt();
    }

    /**
     * 获取token
     *
     * @param request 请求
     * @return token
     * @throws SignatureException 签名错误异常
     */
    public static String getTokenFromRequest(HttpServletRequest request) throws SignatureException {
        // Token 验证
        String token = request.getHeader("Authorization");
        if (StringUtils.isEmpty(token)) {
            token = request.getParameter("token");
        }
        if (StringUtils.isEmpty(token)) {
            throw new SignatureException("token不能为空");
        }
        return token;
    }

    /**
     * 获取密钥
     *
     * @return 密钥
     */
    private static SecretKey generalKey() {
        byte[] encodedKey = Base64.decodeBase64(SECRET);
        return new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
    }

    /**
     * 设置用户账号到threadLocal
     *
     * @param account 用户账号
     */
    public static void setAccount(String account) {
        threadLocal.set(account);
    }

    /**
     * 从threadLocal中获取用户账号
     *
     * @return 用户账号
     */
    public static String getAccount() {
        return threadLocal.get();
    }

    /**
     * 删除threadLocal中存储的用户账号
     */
    public static void removeAccount() {
        threadLocal.remove();
    }
}
