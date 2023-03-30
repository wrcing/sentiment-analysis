package com.wrc.cloud.filter;


import cn.hutool.core.text.AntPathMatcher;
import com.wrc.cloud.entities.ResponseResult;
import com.wrc.cloud.util.JwtTokenUtil;
import com.wrc.cloud.util.MonoBuildUtil;
import io.jsonwebtoken.Claims;
import io.netty.handler.codec.http.cookie.Cookie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * @author : wrc
 * @description: TODO
 * @date : 2023/1/25 12:43
 */
@Slf4j
@Component
public class LoginGateWayFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String requestUrl = exchange.getRequest().getPath().value();
        AntPathMatcher pathMatcher = new AntPathMatcher();

        // auth服务所有放行
        if (pathMatcher.match("/api/auth/**", requestUrl)) {
            return chain.filter(exchange);
        }

        // 添加数据接口 开放
        HttpMethod requestMethod = exchange.getRequest().getMethod();
        if (requestMethod != null && requestMethod.matches("POST")){
            if (pathMatcher.match("/api/weibo/comment", requestUrl)
                    || pathMatcher.match("/api/twitter/comment", requestUrl)
                    || pathMatcher.match("/api/bili/reply", requestUrl)){

                return chain.filter(exchange);
            }
        }

        if (pathMatcher.match("/api/bili/reply/add", requestUrl)) {
            return chain.filter(exchange);
        }
        // 配上nginx后不再需要
//        if (pathMatcher.match("/index.html", requestUrl) || pathMatcher.match("/static/**", requestUrl)) {
//            return chain.filter(exchange);
//        }

//        log.info(exchange.getRequest().getCookies().toString());

        //2 检查cookie登录状态，有登录token则放行（token过期返回是null）
        HttpCookie loginCookie = exchange.getRequest().getCookies().getFirst("token");
        if (loginCookie != null) {
            Claims loginPlayLoad = JwtTokenUtil.getPlayLoad(loginCookie.getValue());
            if (loginPlayLoad != null) {
                // 已登录
                log.info(exchange.getRequest().getPath()+"===cookie的token");
                return chain.filter(exchange);
            }
            else {
                // 有cookie但登录过期
                // 将cookie中token删除
                ResponseCookie cookie = ResponseCookie.from("token", "")
                        .maxAge(0L)
                        .httpOnly(true)
                        .secure(true)
                        .path("/")
                        .sameSite("strict")
                        .build();
                exchange.getResponse().addCookie(cookie);
                return MonoBuildUtil.buildReturnMono(ResponseResult.error("登录过期"), exchange);
            }
        }

        //3 若无登录，则检查resourceToken是否存在，存在则为跨域请求，允许访问
//        log.info(exchange.getRequest().getPath().value()+exchange.getResponse().getCookies().toString());
        String resourceToken = exchange.getRequest().getHeaders().getFirst("X-Token");
        if (resourceToken != null){
            // 有token
            Claims resourcePlayLoad = JwtTokenUtil.getPlayLoad(resourceToken);
            if (resourcePlayLoad != null){
                // token合法
                String resource = (String) resourcePlayLoad.get("resource");
                if(resource != null && !resource.equals("")){
                    // token为资源访问token
                    // 放行
                    log.info(exchange.getRequest().getPath()+"====header中resourceToken存在");
                    return chain.filter(exchange);
                }
            }
        }
        // 无token，不予路由，直接返回
        return MonoBuildUtil.buildReturnMono(ResponseResult.error("未登录"),exchange);

    }

    @Override
    public int getOrder() {
        //小 优先级高
        return 0;
    }
}
