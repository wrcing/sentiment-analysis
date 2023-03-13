package com.wrc.cloud.service.impl;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author : wrc
 * @description: TODO
 * @date : 2023/2/6 23:36
 */
@Service
public class RedisServiceImpl {
    private static final String JWT_CACHE_KEY = "jwt:userId:";
    private static final String USER_ID = "userId";
    private static final String USER_NAME = "username";
    private static final String ACCESS_TOKEN = "access_token";
    private static final String REFRESH_TOKEN = "refresh_token";
    private static final String EXPIRE_IN = "expire_in";

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public void cacheToken(String userId, Map<String, Object> tokenMap) {
        stringRedisTemplate.opsForHash().put(JWT_CACHE_KEY + userId, ACCESS_TOKEN, tokenMap.get(ACCESS_TOKEN));
        stringRedisTemplate.opsForHash().put(JWT_CACHE_KEY + userId, REFRESH_TOKEN, tokenMap.get(REFRESH_TOKEN));
//        stringRedisTemplate.expire(userId, jwtProperties.getExpiration() * 2, TimeUnit.MILLISECONDS);
    }

    public boolean deleteCacheToken(String userId){
        return Boolean.TRUE.equals(stringRedisTemplate.delete(JWT_CACHE_KEY + userId));
    }

    /**
     * 判断令牌是否不存在 redis 中
     *
     * @param token 刷新令牌
     * @return true=存在，false=不存在
     */
    public Boolean isRefreshTokenExistCache(String token) {
//        String userId = getUserIdFromToken(token);
//        String refreshToken = (String)stringRedisTemplate.opsForHash().get(JWT_CACHE_KEY + userId, REFRESH_TOKEN);
//        return refreshToken == null || !refreshToken.equals(token);
        return true;
    }

}
