package xin.pwdkeeper.wechat.service;

import xin.pwdkeeper.wechat.bean.R;
import xin.pwdkeeper.wechat.bean.RequestParams;
import java.util.concurrent.TimeUnit;

public interface RedisService {
    // 获取缓存数据
    <T> T get(String key, Class<T> clazz);

    // 设置缓存数据
    void set(String key, Object value);

    // 删除缓存数据
    void delete(String key);

    Object get(String verifyCode);

    /**
     * 生成验证码并存储到Redis中
     * @param key 缓存的key
     * @param value 验证码
     * @param timeout 失效时间
     * @param unit 时间单位
     */
    void generateVerifyCode(String key, String value, long timeout, TimeUnit unit);

    R generateVerifyCode(RequestParams request);

    /**
     * 检查缓存中是否存在某个key
     * @param key 缓存的key
     * @return 是否存在
     */
    boolean hasKey(String key);
}