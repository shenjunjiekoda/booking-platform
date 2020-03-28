package cn.shenjunjie.booking.utils;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/3/27 7:34
 */
@Component
public class RedisUtil {

    private RedisTemplate redisTemplate;

    @Autowired
    public void setRedisTemplate(RedisTemplate redisTemplate){
        RedisSerializer stringSerializer = new StringRedisSerializer();
        FastJsonRedisSerializer<Object> jsonRedisSerializer = new FastJsonRedisSerializer<>(Object.class);
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setValueSerializer(jsonRedisSerializer);
        redisTemplate.setHashKeySerializer(stringSerializer);
        redisTemplate.setHashValueSerializer(jsonRedisSerializer);
        this.redisTemplate = redisTemplate;
    }

    public void set(String key, Object value) {
        ValueOperations operations = redisTemplate.opsForValue();
        operations.set(key, value);
    }

    public Object get(String key){
        return redisTemplate.opsForValue().get(key);
    }

}
