package com.wrc.cloud.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author : wrc
 * @description: TODO
 * @date : 2023/2/7 20:13
 */
//@Component
@Slf4j
public class PostGlobalFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return chain.filter(exchange)
                .then(Mono.fromRunnable(
                        () -> log.info("Global Post Filter order=1")
                ));
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
