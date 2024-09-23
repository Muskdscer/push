package com.push.sso;

import com.push.common.constants.JwtClaimsKeyConstant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Description: JWT token操作util
 * Create DateTime: 2020-03-23 17:57
 *
 * 

 */
@Component
public class JwtTokenUtils {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.token.header}")
    private String tokenHeader;

    @Value("${jwt.token.start}")
    private String tokenStart;

    /**
     * 生成token
     *
     * @param username 用户名
     * @param userId   用户ID
     * @param roleId   用户角色ID
     * @return token
     */
    public String generateToken(String username, Long userId, Long roleId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsKeyConstant.CLAIM_USER_ID, userId);
        claims.put(JwtClaimsKeyConstant.CLAIM_ROLE_ID, roleId);
        return doGenerateToken(claims, username);
    }

    /**
     * 执行生成token
     *
     * @param claims  所有的Claims
     * @param subject 主体
     * @return 生成的token
     */
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(new Date(Long.MAX_VALUE)) //设为永久不过期
                .signWith(SignatureAlgorithm.HS512, TextCodec.BASE64.encode(secret))
                .compact();
    }

    /**
     * 验证token是否有效
     *
     * @param token    token
     * @param username 用户名
     * @return 是否有效（true/false）
     */
    public Boolean validateToken(String token, String username) {
        String name = getUsernameFromToken(token);
        return (username.equals(name) && !isTokenExpired(token));
    }

    /**
     * 从token中获取用户名
     *
     * @param token token
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    /**
     * 从token中获取用户ID
     *
     * @param request HttpServletRequest
     * @return 用户ID
     */
    public Long getUserIdFromToken(HttpServletRequest request) {
        String authHeader = request.getHeader(tokenHeader);
        String authToken = null;
        String headerStart = tokenStart + " ";
        if (StringUtils.isNotBlank(authHeader) && authHeader.startsWith(headerStart)) {
            authToken = authHeader.substring(headerStart.length()).trim();
        }
        return getAllClaimsFromToken(authToken).get(JwtClaimsKeyConstant.CLAIM_USER_ID, Long.class);
    }

    /**
     * 从token中获取角色ID
     *
     * @param request bo
     * @return 角色ID
     */
    public Long getRoleIdFromToken(HttpServletRequest request) {
        String authHeader = request.getHeader(tokenHeader);
        String authToken = null;
        String headerStart = tokenStart + " ";
        if (StringUtils.isNotBlank(authHeader) && authHeader.startsWith(headerStart)) {
            authToken = authHeader.substring(headerStart.length()).trim();
        }
        return getAllClaimsFromToken(authToken).get(JwtClaimsKeyConstant.CLAIM_ROLE_ID, Long.class);
    }

    /**
     * 从token中获取具体的Claim
     *
     * @param token          token
     * @param claimsResolver 解析器
     * @param <T>            泛型
     * @return 具体的Claim的值
     */
    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    /**
     * 从token中获取所有的Claims
     *
     * @param token token
     * @return 所有的Claims
     */
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(TextCodec.BASE64.encode(secret))
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 判断token是否过期
     *
     * @param token token
     * @return token是否过期（true/false）
     */
    private Boolean isTokenExpired(String token) {
        Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    /**
     * 获取token的过期时间
     *
     * @param token token
     * @return token的过期时间
     */
    private Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

}

