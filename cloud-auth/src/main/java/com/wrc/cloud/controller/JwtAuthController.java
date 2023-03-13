package com.wrc.cloud.controller;

import cn.hutool.http.HttpResponse;
import cn.hutool.jwt.JWTUtil;
import com.wrc.cloud.entities.ResponseResult;
import com.wrc.cloud.entity.ResponseCodeEnum;
import com.wrc.cloud.entity.User;
import com.wrc.cloud.service.UserService;
import com.wrc.cloud.service.impl.RedisServiceImpl;
import com.wrc.cloud.util.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : wrc
 * @description: login logout refresh，逻辑先暂放到controller中
 * @date : 2023/2/6 20:49
 */
@RestController
@RequestMapping("/api/auth")
@Slf4j
public class JwtAuthController {

    @Resource
    private UserService userService;

    @Resource
    RedisServiceImpl redisService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private static final String JWT_REFRESH_TOKEN_PREFIX = "jwt:refresh:";
    private static final String JWT_LOGIN_TOKEN_PREFIX = "jwt:login:";
    private static final String JWT_RESOURCE_TOKEN_PREFIX = "jwt:resource:";


    /**
     * 使用用户名密码换 JWT 登录令牌，访问资源还要有个资源令牌
     * 这个登录令牌用来访问用户信息，就放在cookie中
     * 资源跨域访问时带上资源令牌到header中
     */
    @PostMapping("/login")
    public ResponseResult<?> login(@RequestBody Map<String,String> map, HttpServletResponse response){
        if (map == null){
            return ResponseResult.error(ResponseCodeEnum.LOGIN_ERROR.getCode(), ResponseCodeEnum.LOGIN_ERROR.getMessage());
        }
        // 从请求体中获取用户名密码
        String username  = map.get("username");
        String password = map.get("password");

        // 如果用户名和密码为空
        if(StringUtils.isEmpty(username) || StringUtils.isEmpty(password)){
            return ResponseResult.error(ResponseCodeEnum.LOGIN_ERROR.getCode(), ResponseCodeEnum.LOGIN_ERROR.getMessage());
        }
        // 根据 username 去数据库查找该用户
        User user = userService.queryByUsername(username);
        if(user == null) {
            // 如果未找到用户
            return ResponseResult.error(ResponseCodeEnum.LOGIN_ERROR.getCode(), ResponseCodeEnum.LOGIN_ERROR.getMessage());
        }
//        log.info(user.toString());
        if(!password.equals(user.getPassword())){
            // 如果密码匹配失败
            return ResponseResult.error(ResponseCodeEnum.LOGIN_ERROR.getCode(), ResponseCodeEnum.LOGIN_ERROR.getMessage());
        }
        // 通过 jwtTokenUtil 生成 JWT 令牌
        Map<String, String> playLoad = new HashMap<>();
        playLoad.put("username", user.getUsername());
        playLoad.put("userId", user.getUid().toString());
        String token = JwtTokenUtil.createJWT(playLoad);


        // 要设置仅https和禁止js
        ResponseCookie cookie = ResponseCookie.from("token", token)
                .maxAge(JwtTokenUtil.EXPIRE_TIME_MILLIS /1000)
//                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite("Strict")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        // 返回资源访问的token
        Map<String, String> load = new HashMap<>();
        load.put("userId", user.getUid().toString());
        load.put("resource", "all wrc cloud resource");
        String resourceToken = JwtTokenUtil.createJWT(load);
        HashMap<String, String> result = new HashMap<>();
        result.put("token", resourceToken);
        // 同时存入cookie，js可以访问
        ResponseCookie resourceCookie = ResponseCookie.from("resourceToken", resourceToken)
                .maxAge(JwtTokenUtil.EXPIRE_TIME_MILLIS / 1000)
//                .httpOnly(true)
                .secure(true)
                .path("/")
//                .sameSite("strict")
                .sameSite("Lax")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, resourceCookie.toString());

        // refresh存入redis
        // redisService.cacheToken(user.getUid().toString(), tokenMap);

        return ResponseResult.success(result);

    }

    /**
     * 由于右上角显示的简单用户信息
     *
     * not finished
     * */
    @GetMapping("/user/info")
    public String userInfo(){
        String result = "{\"code\":20000,\"data\":{\"roles\":[\"admin\"],\"introduction\":\"I am a super administrator\",\"avatar\":\"https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif\",\"name\":\"Super Admin\"}}";
        return result;
    }

    /**
     * 刷新JWT令牌,用旧的令牌换新的令牌
     */
    @GetMapping("/refreshToken")
    public  ResponseResult refreshToken(HttpServletRequest request){
        // 拿到cookie中的refreshToken
        Cookie[] cookies = request.getCookies();
        String token = null;
        for (Cookie c : cookies){
            if (c.getName().equals("refreshToken")){
                token = c.getValue();
                break;
            }
        }
        Claims playLoad = JwtTokenUtil.getPlayLoad(token);
        if (playLoad == null) {
            return ResponseResult.error("无刷新权限");
        }

        // 这里为了保证 refreshToken 只能用一次，刷新后，会从 redis 中删除。
        // 如果用的不是 redis 中的 refreshToken 进行刷新令牌，则不能刷新。
//        boolean isRefreshTokenNotExisted = jwtTokenUtil.isRefreshTokenNotExistCache(token);
//        if(isRefreshTokenNotExisted){
//            return buildErrorResponse(ResponseCodeEnum.REFRESH_TOKEN_INVALID);
//        }
//
//        String us = jwtTokenUtil.getUserIdFromToken(token);
//        Map<String, Object> tokenMap = jwtTokenUtil.refreshTokenAndGenerateToken(userId, username);
        // 返回新生成的token，及refreshToken
        return ResponseResult.success("待处理");
    }

    /**
     * 登出，删除 redis 中的 未使用一次性token（保证幂等性的token）
     * 删除cookie中token就好了
     *
     * 若是登录jwt也存到redis，那就没有使用jwt的必要了，随机生成一个唯一码就好了，反正是要查redis的
     *
     * 至于说拿着退出前的token还能进行访问的问题，前端存储已经删除，在呢？
     * 若被别人拿到这个token，登出又有什么意义呢？他下次还能拿到。
     *
     * 只保证 refreshToken 不能使用，accessToken 还是能使用的。
     * 如果用户拿到了之前的 accessToken，则可以一直使用到过期，但是因为 refreshToken 已经无法使用了，部分保证了 accessToken 的时效性。
     * 下次登录时，需要重新获取新的 accessToken 和 refreshToken，这样才能利用 refreshToken 进行续期。
     */
    @PostMapping("/logout")
    public ResponseResult logout(@RequestHeader Map<String, String> headers, HttpServletResponse response){

        String refreshHeader = headers.get("refreshToken");
        Claims playLoad = JwtTokenUtil.getPlayLoad(refreshHeader);
        if (playLoad != null){
            String userId = (String) playLoad.get("userId");
            if (userId != null){
                // 删除redis中refreshToken
                Boolean delete = stringRedisTemplate.delete(JWT_REFRESH_TOKEN_PREFIX + userId);
            }
        }

        // 删除cookie
        ResponseCookie cookie = ResponseCookie.from("token", "token")
                .maxAge(0L)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite("Strict")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        ResponseCookie resourceCookie = ResponseCookie.from("resourceToken", "resourceToken")
                .maxAge(0L)
                .secure(true)
                .path("/")
                .sameSite("Lax")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, resourceCookie.toString());

        return ResponseResult.success("logout success");
    }
}
