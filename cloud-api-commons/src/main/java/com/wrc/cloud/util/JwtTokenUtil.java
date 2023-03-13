package com.wrc.cloud.util;

import cn.hutool.core.lang.UUID;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;


import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.Map;

/**
 * @author : wrc
 * @description: TODO
 * @date : 2023/2/6 21:45
 */

public class JwtTokenUtil {

    private static final String JWT_CACHE_KEY = "jwt:userId:";

    //过期毫秒数
//    @Value("${jwt.token.expireTime}")
    public static long EXPIRE_TIME_MILLIS =3*60*60*1000;  //1h

    //密钥字符串
//    @Value("${jwt.token.secret}")
    public static String TOKEN_SECRET = "0s58aIHircc1emsIrZ6+KrNvzRxIaivxjWY/j4eQNxg=";

    //签名安全密钥
    public static SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(TOKEN_SECRET));
    }

    /**生成安全密钥，只执行一次*/
    public static void genSecretKey(){
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        String secretString = Encoders.BASE64.encode(key.getEncoded());
        System.out.println("安全密钥："+secretString);
    }


    /**创建JWT*/
    public static String createJWT(Map<String, String> map){
        return createJWT(map, EXPIRE_TIME_MILLIS);
    }
    public static String createJWT(Map<String, String> map, Long timeMillis){
        //创建一个JWT构造器
        JwtBuilder builder = Jwts.builder();
        //header
        builder.setHeaderParam("alg","HS256");  //签名加密算法的类型
        builder.setHeaderParam("typ","JWT");  //token类型
        //payload
        builder.setExpiration(new Date(System.currentTimeMillis() + timeMillis));  //过期时间
        builder.setIssuedAt(new Date());  //签发时间
        builder.setId(UUID.randomUUID().toString());  //JWT ID
        builder.setSubject("auth");  //主题
        if (map != null) {
            for (String key : map.keySet()) {
                builder.claim(key, map.get(key));
            }
        }
        builder.signWith(getSecretKey());  //设置签名的密钥
        //生成签名
        String token=builder.compact();
        return token;
    }

    /**
     * 解析jwt
     * */
    public static void showJwt(String jwt){
        // 创建解析器
        JwtParserBuilder jwtParserBuilder = Jwts.parserBuilder();  //jwt解析器
        jwtParserBuilder.setSigningKey(getSecretKey());  //设置签名的密钥
        try {
            Jws<Claims> claimsJws = jwtParserBuilder.build().parseClaimsJws(jwt);//解析内容,获得payload
            System.out.println("头部："+claimsJws.getHeader());
            System.out.println("数据："+claimsJws.getBody());
            System.out.println("签名："+claimsJws.getSignature());

            JwsHeader header = claimsJws.getHeader();
            System.out.println(header.getAlgorithm());
            System.out.println(header.get("typ"));

            Claims body = claimsJws.getBody();
            System.out.println(body.getExpiration());
            System.out.println(body.toString());
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * 获取jwt载荷
     * null为解析错误，包括超时
     * */
    public static Claims getPlayLoad(String jwt){
        if (jwt == null) return null;
        // 创建解析器
        JwtParserBuilder jwtParserBuilder = Jwts.parserBuilder();  //jwt解析器
        jwtParserBuilder.setSigningKey(getSecretKey());  //设置签名的密钥
        try {
            Jws<Claims> claimsJws = jwtParserBuilder.build().parseClaimsJws(jwt);//解析内容,获得payload
            Claims body = claimsJws.getBody();
            return body;
        } catch (Exception e){
            // 签名有问题
//            e.printStackTrace();
            return null;
        }
    }

}
