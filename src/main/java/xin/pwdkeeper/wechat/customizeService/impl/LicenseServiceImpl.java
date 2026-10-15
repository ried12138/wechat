package xin.pwdkeeper.wechat.customizeService.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xin.pwdkeeper.wechat.bean.R;
import xin.pwdkeeper.wechat.bean.RequestParams;
import xin.pwdkeeper.wechat.customizeService.LicenseService;
import xin.pwdkeeper.wechat.customizeService.RedisService;
import xin.pwdkeeper.wechat.util.AesUtil;
import xin.pwdkeeper.wechat.util.RedisKeysUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 学习码(防盗版授权)服务实现
 * 说明：本实现完全独立，不复用也不改动验证码资产登录相关的既有逻辑，
 * 加密统一复用现有的 {@link AesUtil}。
 *
 * @Author weiranliu
 * @Version 1.0
 */
@Slf4j
@Service
public class LicenseServiceImpl implements LicenseService {

    // 学习验证码前缀
    private static final String LICENSE_CODE_PREFIX = "XXM";
    // 学习验证码在Redis中的有效期（分钟）
    private static final long ACTIVATION_TTL_MINUTES = 5;
    // 心跳间隔（分钟），与客户端约定保持一致
    private static final long HEARTBEAT_INTERVAL_MINUTES = 11;
    // 心跳凭证的时间戳过期阈值（分钟），超过则认为授权失效
    // 取心跳间隔的2倍以上，保证即使错过一次心跳、下一次成功仍能续命
    private static final long HEARTBEAT_EXPIRE_MINUTES = HEARTBEAT_INTERVAL_MINUTES * 2 + 3;
    // 凭证明文各字段的分隔符（openId与验证码本身都不包含该字符）
    private static final String TOKEN_SEPARATOR = "/";
    // 一分钟的毫秒数
    private static final long ONE_MINUTE_MILLIS = 60 * 1000L;

    @Autowired
    private RedisService redisService;

    @Override
    public R generateLicenseCode(RequestParams request) {
        String openId = request.getOpenId();
        if (openId == null || openId.isEmpty()) {
            log.warn("[学习码-生成] 生成失败, openId为空");
            return R.failed(null, "openId不能为空");
        }
        // 每次回复都生成新码；若随机数碰撞已存在的key则重新生成
        String code = generateUniqueCode();
        try {
            // key由验证码本身派生，value为发送人openId，有效期5分钟
            redisService.settimelinessCach(RedisKeysUtil.LICENSE_CODE_KEY + code, openId, ACTIVATION_TTL_MINUTES, TimeUnit.MINUTES);
        } catch (Exception e) {
            log.error("[学习码-生成] 存储失败, 用户:{} 学习码生成失败, code:{}", openId, code, e);
            return R.failed(null, "学习码生成失败");
        }
        log.info("[学习码-生成] 生成成功, openId={}, code={}", openId, code);
        Map<String, Object> map = new HashMap<>();
        map.put(RedisKeysUtil.LICENSE_CODE, code);
        return R.ok(map);
    }

    @Override
    public R verifyLicenseCode(RequestParams request) {
        Map<String, Object> data = (Map<String, Object>) request.getRequestParam();
        String code = data == null ? null : (String) data.get(RedisKeysUtil.LICENSE_CODE);
        if (code == null || code.isEmpty()) {
            log.warn("[学习码-校验] 校验失败, 验证码为空, openId={}", request.getOpenId());
            return R.failed(null, "验证码不能为空");
        }
        String redisKey = RedisKeysUtil.LICENSE_CODE_KEY + code;
        Object value = redisService.get(redisKey);
        if (value == null) {
            log.warn("[学习码-校验] 校验失败, 验证码已过期或不存在, openId={}", request.getOpenId());
            return R.failed(null, "验证码已过期或不存在");
        }
        String openId = String.valueOf(value);
        // 校验通过后立即从Redis删除该验证码
        redisService.delete(redisKey);
        log.info("[学习码-校验] 验证码校验通过, openId={}, 开始签发凭证", openId);
        try {
            String token = buildToken(code, openId, System.currentTimeMillis());
            Map<String, Object> result = new HashMap<>();
            result.put(RedisKeysUtil.LICENSE_TOKEN, token);
            log.info("[学习码-校验] 凭证签发成功, openId={}", openId);
            return R.ok(result);
        } catch (Exception e) {
            log.error("[学习码-校验] 凭证签发失败(加密异常), openId={}, code:{}", openId, code, e);
            return R.failed(null, "校验失败");
        }
    }

    @Override
    public R heartbeatLicense(RequestParams request) {
        Map<String, Object> data = (Map<String, Object>) request.getRequestParam();
        String token = data == null ? null : (String) data.get(RedisKeysUtil.LICENSE_TOKEN);
        if (token == null || token.isEmpty()) {
            log.warn("[学习码-心跳] 校验失败, 凭证为空, openId={}", request.getOpenId());
            return R.failed(null, "凭证不能为空");
        }
        // 敏感凭证脱敏：仅记录 token 长度用于排查传输截断，严禁打印密文
        log.info("[学习码-心跳] 开始校验, tokenLength={}", token.length());
        try {
            String plain = AesUtil.decrypt(token);
            String[] parts = plain.split(TOKEN_SEPARATOR);
            if (parts.length != 3) {
                log.warn("[学习码-心跳] 校验失败, 凭证非法, tokenLength={}", token.length());
                return R.failed(null, "凭证非法");
            }
            String openId = parts[1];
            long timestamp = Long.parseLong(parts[2]);
            long elapsedMs = System.currentTimeMillis() - timestamp;
            // openId 解密后才能拿到，这里才是"谁发起了心跳"的关键日志
            log.info("[学习码-心跳] 凭证解析成功, openId={}, 距上次心跳={}分钟", openId, elapsedMs / ONE_MINUTE_MILLIS);
            if (elapsedMs > HEARTBEAT_EXPIRE_MINUTES * ONE_MINUTE_MILLIS) {
                log.warn("[学习码-心跳] 授权过期, openId={}, 距上次心跳={}分钟, 过期阈值={}分钟", openId, elapsedMs / ONE_MINUTE_MILLIS, HEARTBEAT_EXPIRE_MINUTES);
                return R.failed(null, "授权已过期，请重新获取验证码");
            }
            // 轮转：保留前缀、替换6位随机数字，刷新时间戳后重新加密
            String newToken = buildToken(rotateCode(), openId, System.currentTimeMillis());
            Map<String, Object> result = new HashMap<>();
            result.put(RedisKeysUtil.LICENSE_TOKEN, newToken);
            log.info("[学习码-心跳] 心跳成功, openId={}, 凭证已轮转", openId);
            return R.ok(result);
        } catch (Exception e) {
            // 解密失败等异常，同样只做脱敏处理，仅记录 token 长度与异常信息
            log.warn("[学习码-心跳] 凭证校验失败, tokenLength={}, err={}", token.length(), e.getMessage());
            return R.failed(null, "凭证校验失败");
        }
    }

    /**
     * 组装并加密凭证明文：验证码/openId/时间戳
     */
    private String buildToken(String code, String openId, long timestamp) throws Exception {
        return AesUtil.encrypt(code + TOKEN_SEPARATOR + openId + TOKEN_SEPARATOR + timestamp);
    }

    /**
     * 生成一个新的学习验证码（XXM+6位随机数字）
     */
    private String generateUniqueCode() {
        String code;
        do {
            code = LICENSE_CODE_PREFIX + randomSixDigits();
        } while (redisService.hasKey(RedisKeysUtil.LICENSE_CODE_KEY + code));
        return code;
    }

    /**
     * 心跳轮转：保持 XXM 前缀，重新生成6位随机数字
     */
    private String rotateCode() {
        return LICENSE_CODE_PREFIX + randomSixDigits();
    }

    private String randomSixDigits() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
}
