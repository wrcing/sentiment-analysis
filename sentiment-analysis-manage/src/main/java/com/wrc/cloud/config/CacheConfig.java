package com.wrc.cloud.config;

import cn.hutool.core.date.DateUtil;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.lang.NonNull;


import java.lang.reflect.Method;
import java.time.Duration;
import java.util.Date;
import java.util.List;

/**
 * @author : wrc
 * @description: TODO
 * @date : 2023/4/16 16:19
 */
@Configuration
@EnableCaching
public class CacheConfig {

    /**
     * 选择redis作为默认缓存工具
     * @param redisConnectionFactory
     * @return
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        // 分别创建String和JSON格式序列化对象，对缓存数据key和value进行转换
        RedisSerializer<String> strSerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer jacksonSerial = new Jackson2JsonRedisSerializer(Object.class);
        // 解决查询缓存转换异常的问题
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY); // 上面注释过时代码的替代方法
        jacksonSerial.setObjectMapper(om);
        // 定制缓存数据序列化方式及时效
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(60)) // 设置缓存数据的时效 30mins
                .computePrefixWith(name -> name+":")
                .serializeKeysWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(strSerializer)) // 对当前对象的key使用strSerializer这个序列化对象，进行转换
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(jacksonSerial)) // 对value使用jacksonSerial这个序列化对象，进行转换
                .disableCachingNullValues();
        RedisCacheManager cacheManager = RedisCacheManager
                .builder(redisConnectionFactory).cacheDefaults(config).build();
        return cacheManager;
    }


    /**
     * 参数为Date或Integer这样的一般的包装类 或 list
     * 不包括自定义的实体类
     * 不可有 int long 等基本类型
     * */
    @Bean
    public KeyGenerator simpleObjAndListKeyGenerator(){
        return new KeyGenerator() {
            @Override
            public Object generate(@NonNull Object target,@NonNull Method method,@NonNull Object... params) {
                String key = method.getName()+":";
                // 日期放到前面
                for (Object param : params){
                    if (param instanceof Date){
                        key += DateUtil.format((Date) param, "yyyyMMddHHmmss:");
                    }
                }
                for (Object param : params){
                    if (param instanceof Date) {
                        continue;
                    }
                    if (param instanceof List){
                        for (Object obj : (List) param){
                            if (obj instanceof Date){
                                key += DateUtil.format((Date)obj, "yyyyMMddHHmmss");
                            }
                            else key += obj.toString();
                        }
                    }
                    else {
                        key += param.toString();
                    }
                }
                return key;
            }
        };
    }

}
