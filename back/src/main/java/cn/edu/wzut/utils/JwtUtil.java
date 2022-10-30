package cn.edu.wzut.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author zcz
 * @since 2022/7/3 17:07
 */
@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtUtil {

    private long expiration; //jwt有效时间（s）
    private String secret; //秘钥
    private String header;
    //生成jwt
    public String createJwt(String username){
        Date nowDate = new Date();
        Date expirationDate = new Date(nowDate.getTime()+1000*expiration);
        return Jwts.builder()
                .setHeaderParam("typ","JWT")
                .setSubject(username)//面向用户
                .setIssuedAt(nowDate) //创建时间
                .setExpiration(expirationDate) //过期时间
                .signWith(SignatureAlgorithm.HS512,secret)//签名方法
                .compact();

    }

    //解析jwt
    public Claims getClaimsByBody(String jwt) {
        try {
            return Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(jwt)
                    .getBody();
        }catch(Exception e) {
            return null;
        }
    }

    //判断jwt是否过期
    public boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }
}
