package xin.pwdkeeper.wechat.service;

import java.util.concurrent.TimeUnit;

public interface RedisService {
    // 获取缓存数据
    <T> T get(String key, Class<T> clazz);

    // 设置缓存数据
    void set(String key, Object value);

    // 删除缓存数据
    void delete(String key);

    Object get(String obj);

    void settimelinessCach(String key, String value, long timeout, TimeUnit unit);

    /**
     * 检查缓存中是否存在某个key
     * @param key 缓存的key
     * @return 是否存在
     */
    boolean hasKey(String key);
}