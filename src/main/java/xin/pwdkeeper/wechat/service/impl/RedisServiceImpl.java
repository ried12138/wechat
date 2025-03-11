package xin.pwdkeeper.wechat.service.impl;

import com.mysql.cj.protocol.x.OkBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import xin.pwdkeeper.wechat.bean.R;
import xin.pwdkeeper.wechat.bean.RequestParams;
import xin.pwdkeeper.wechat.service.RedisService;
import xin.pwdkeeper.wechat.toolutil.SignMD5Util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class RedisServiceImpl implements RedisService {


    public static final String VERIFY_CODE_KEY = "verifyCode_";
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

    @Override
    public void generateVerifyCode(String key, String value, long timeout, TimeUnit unit) {
        if (!hasKey(key)) {
            redisTemplate.opsForValue().set(key, value, timeout, unit);
        }
    }

    /**
     * 生成验证码接口的实现方法
     * 此方法旨在为用户生成并返回一个验证码，如果用户已有一个未过期的验证码，则直接返回该验证码
     * @param request 包含用户信息的请求参数对象，用于获取用户ID
     * @return 返回一个结果对象R，包含生成的验证码或错误信息
     */
    @Override
    public R generateVerifyCode(RequestParams request) {
        // 构造验证码的键值，用于在Redis中存储和查询验证码
        String verifyCodeKey = RedisServiceImpl.VERIFY_CODE_KEY + request.getUserId();
        Map<String, Object> map = new HashMap<String, Object>();
        // 检查Redis中是否已存在该用户的验证码
        if (hasKey(verifyCodeKey)) {
            // 如果存在，直接返回存在的验证码
            map.put("code", get(verifyCodeKey));
            return R.ok(map);
        }
        // 生成随机验证码
        String verifyCode = SignMD5Util.generateVerificationCode();
        try {
            // 将生成的验证码保存到Redis中，并设置过期时间
            generateVerifyCode(verifyCodeKey, verifyCode, 30, TimeUnit.MINUTES);
        } catch (Exception e) {
            // 如果验证码生成过程中发生异常，记录错误日志并返回错误信息
            log.error("用户:{} ,验证码生成失败: {}",request.getUserId(),verifyCodeKey, e);
            return R.failed(null, "验证码生成失败。");
        }
        map.put("code", verifyCode);
        // 返回生成的验证码
        return R.ok(map);
    }

    @Override
    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }
}