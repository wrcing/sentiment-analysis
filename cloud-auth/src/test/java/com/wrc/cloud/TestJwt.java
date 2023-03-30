package com.wrc.cloud;

import cn.hutool.core.lang.UUID;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

/**
 * @author : wrc
 * @description: TODO
 * @date : 2023/2/7 12:06
 */
@SpringBootTest
public class TestJwt {

    //密钥字符串
    @Value("${jwt.token.secret}")
    public String KEYSTRING = "0s58aIHircc1emsIrZ6+KrNvzRxIaivxjWY/j4eQNxg=";

    //签名安全密钥
    public SecretKey getKey() {
        SecretKey secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(KEYSTRING));
        return secretKey;
    }

    /**生成安全密钥，只执行一次*/
    @Test
    public void genSecretKey(){
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        String secretString = Encoders.BASE64.encode(key.getEncoded());
        System.out.println("安全密钥："+secretString);
    }

    //过期毫秒数
    public static final long EXPIRETIME=60*60*1000;  //1天

//    public static final long EXPIRETIME=5*1000;  //5秒
    /**创建JWT*/
    @Test
    public void createJWT(){
        System.out.println("yaml文件里面密钥的值："+KEYSTRING);
        //创建一个JWT构造器
        JwtBuilder builder = Jwts.builder();
        //header
        builder.setHeaderParam("alg","HS256");  //签名加密算法的类型
        builder.setHeaderParam("typ","JWT");  //token类型
        //payload
        builder.setExpiration(new Date(System.currentTimeMillis()+EXPIRETIME));  //过期时间
        builder.setIssuedAt(new Date());  //签发时间
        builder.setId(UUID.randomUUID().toString());  //JWT ID
        builder.setSubject("auth");  //主题
        builder.claim("username","admin");  //自定义信息
        builder.claim("userId","248221412");  //自定义信息
        builder.claim("role","superadmin");  //角色，自定义信息
        builder.signWith(getKey());  //设置签名的密钥
        //生成签名
        String token=builder.compact();
        System.out.println("签名token:"+token);
        //eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhdXRoIn0.TYKaWoixlcu8ma27Bf_i_pNujBLvwtkiX8WoXpUpg6I
    }

    @Test
    public void parseJWT(){
        String jwt ="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpYXQiOjE2Njg5Njc2OTcsImV4cCI6MTY2OTMxMzI5NywianRpIjoiN2M1OGJjOWEtNjY0Yi00MDllLWFkN2YtYmQwZjg5NDIwNmY1Iiwic3ViIjoiYXV0aCIsInVzZXJuYW1lIjoiYWRtaW4iLCJyb2xlIjoic3VwZXJhZG1pbiJ9.JGu5BbXWf1UjeCLSyPD1DqbGegbLFTQ9Q6dnP8ppkic";
//        String jwt ="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2NzU3NTMwNzksImlhdCI6MTY3NTc0OTQ3OSwianRpIjoiZTZmMWJmZDUtYTM2Ni00NTYwLWI1ZGYtM2JmNzYwOWJjODNkIiwic3ViIjoiYXV0aCIsInVzZXJuYW1lIjoiYWRtaW4iLCJ1c2VySWQiOiIyNDgyMjE0MTIiLCJyb2xlIjoic3VwZXJhZG1pbiJ9.bx5_78hpOu5H_U9wkNhghxmdRVm42T8LkLF2clYMmLw";
        // 创建解析器
        JwtParserBuilder jwtParserBuilder = Jwts.parserBuilder();  //jwt解析器
        jwtParserBuilder.setSigningKey(getKey());  //设置签名的密钥
        Jws<Claims> claimsJws;
        try {
            claimsJws = jwtParserBuilder.build().parseClaimsJws(jwt);//解析内容,获得payload
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("no valid jwt");
            return;
        }
        System.out.println("头部："+claimsJws.getHeader());
        System.out.println("数据："+claimsJws.getBody());
        System.out.println("签名："+claimsJws.getSignature());

        JwsHeader header = claimsJws.getHeader();
        Claims body = claimsJws.getBody();

        System.out.println(header.getAlgorithm());
        System.out.println(header.get("typ"));

        System.out.println(body.getExpiration());
        System.out.println(body.get("username"));

    }

}
