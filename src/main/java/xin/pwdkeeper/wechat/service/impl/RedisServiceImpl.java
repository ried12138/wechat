package xin.pwdkeeper.wechat.service.impl;

import com.mysql.cj.protocol.x.OkBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import xin.pwdkeeper.wechat.bean.R;
import xin.pwdkeeper.wechat.bean.RequestParams;
import xin.pwdkeeper.wechat.service.RedisService;
import xin.pwdkeeper.wechat.toolutil.RedisKeysUtil;
import xin.pwdkeeper.wechat.toolutil.SignMD5Util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class RedisServiceImpl implements RedisService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public <T> T get(String key, Class<T> clazz) {
        return (T) redisTemplate.opsForValue().get(key);
    }

    @Override
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public Object get(String verifyCode) { return redisTemplate.opsForValue().get(verifyCode); }

    /**
     * 校验key是否存在，如果存在则不保存key
     * @param key 缓存的key
     * @param value 值
     * @param timeout 失效时间
     * @param unit 时间单位
     */
    @Override
    public void settimelinessCach(String key, String value, long timeout, TimeUnit unit) {
        if (!hasKey(key)) {
            redisTemplate.opsForValue().set(key, value, timeout, unit);
        }
    }

    @Override
    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }
}