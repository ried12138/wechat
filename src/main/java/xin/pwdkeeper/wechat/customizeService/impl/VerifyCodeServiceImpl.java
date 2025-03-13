package xin.pwdkeeper.wechat.customizeService.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import xin.pwdkeeper.wechat.bean.R;
import xin.pwdkeeper.wechat.bean.RequestParams;
import xin.pwdkeeper.wechat.customizeService.RedisService;
import xin.pwdkeeper.wechat.customizeService.VerifyCodeService;
import xin.pwdkeeper.wechat.service.WechatUserInfoService;
import xin.pwdkeeper.wechat.util.AesUtil;
import xin.pwdkeeper.wechat.util.JwtTokenUtil;
import xin.pwdkeeper.wechat.util.RedisKeysUtil;
import xin.pwdkeeper.wechat.util.SignMD5Util;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 验证码服务
 *
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2025/3/12   11:12
 * @Version 1.0
 */
@Slf4j
@Service
public class VerifyCodeServiceImpl implements VerifyCodeService {

    @Value("${url.aging.timeliness}")
    private Integer timeliness;
    @Autowired
    private WechatUserInfoService wechatUserInfoService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public R parseVerifyCodeService(RequestParams request) {
        Map<String, Object> data = (Map<String, Object>)request.getRequestBody();
        String checkVerifyCode = (String) data.get("verifyCode");
        if (checkVerifyCode == null || checkVerifyCode.isEmpty()) {
            return R.failed(null, "验证码不能为空");
        }
        String verifyCode = (String) redisService.get(RedisKeysUtil.VERIFY_CODE_KEY + request.getOpenId());
        if (verifyCode == null || verifyCode.isEmpty()) {
            return R.failed(null, "验证码已过期");
        }
        if (!checkVerifyCode.equals(verifyCode)){
            return R.failed(null, "验证码错误");
        }
        //生成令牌(token)
        data.put("token", createToken(request));
        return R.ok(data);
    }

    /**
     * 2025.3.12 如果要接入微信公众号的话，响应体还需要修改格式
     * 目前方法返回的结果：生成的用户唯一url，和用户的验证码
     * 如果发送发不是公众号平台，则要对验证码进行加密
     * 此逻辑只用于生产验证码的地方出自微信公众号，其他地方作为校验方，不得直接获取明文验证码
     * 生成验证码接口的实现方法
     * 此方法旨在为用户生成并返回一个验证码，如果用户已有一个未过期的验证码，则直接返回该验证码
     * @param request 包含用户信息的请求参数对象，用于获取用户ID
     * @return 返回一个结果对象R，包含生成的验证码或错误信息
     */
    public R generateVerifyCode(RequestParams request) throws NoSuchPaddingException, IllegalBlockSizeException, UnsupportedEncodingException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        //判断用户是否存在
        if (!wechatUserInfoService.isByUserOpenId(request.getOpenId())){
            return R.failed(null, "用户不存在");
        }
        // 构造验证码的键值，用于在Redis中存储和查询验证码
        String verifyCodeKey = RedisKeysUtil.VERIFY_CODE_KEY + request.getOpenId();
        Map<String, Object> map = new HashMap<String, Object>();
        // 检查Redis中是否已存在该用户的验证码
        if (redisService.hasKey(verifyCodeKey)) {
            String verifyCode = (String) redisService.get(verifyCodeKey);
            String url = generateSpringUrl(verifyCode, request.getOpenId());
            map.put(RedisKeysUtil.SPRING_URL, url);
            // 如果存在，直接返回存在的验证码
            map.put(RedisKeysUtil.VERIFY_CODE, verifyCode);
            return R.ok(map);
        }
        // 生成随机验证码
        String verifyCode = SignMD5Util.generateVerificationCode();
        try {
            // 将生成的验证码保存到Redis中，并设置过期时间
            redisService.settimelinessCach(verifyCodeKey, verifyCode, 30, TimeUnit.MINUTES);
        } catch (Exception e) {
            // 如果验证码生成过程中发生异常，记录错误日志并返回错误信息
            log.error("用户:{} ,验证码生成失败: {}",request.getOpenId(),verifyCodeKey, e);
            return R.failed(null, "验证码生成失败。");
        }
        //生成跳转url;
        String url = generateSpringUrl(verifyCode, request.getOpenId());
        map.put(RedisKeysUtil.VERIFY_CODE, verifyCode);
        map.put(RedisKeysUtil.SPRING_URL, url);
        // 返回生成的验证码
        return R.ok(map);
    }

    private String generateSpringUrl(String parameter,String openId) throws NoSuchPaddingException, UnsupportedEncodingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        String url = (String) redisService.get(RedisKeysUtil.SPRING_URL);
        String encrypt = AesUtil.encrypt(parameter+"/"+System.currentTimeMillis());
        //用户的openId放入缓存
        redisService.settimelinessCach(encrypt,openId,timeliness, TimeUnit.MINUTES);
        return url+"?openId="+encrypt;
    }


    private String createToken(RequestParams request){return jwtTokenUtil.generateToken(request.getOpenId(),request);}
}
