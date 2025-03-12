package xin.pwdkeeper.wechat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * redis配置初始化
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2025/3/6   16:59
 * @Version 1.0
 */
@Configuration
public class RedisConfig {


    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        // Key序列化为字符串
        template.setKeySerializer(new StringRedisSerializer());
        // Value序列化为JSON
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }

}