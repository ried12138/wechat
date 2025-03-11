package xin.pwdkeeper.wechat.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xin.pwdkeeper.wechat.bean.R;
import xin.pwdkeeper.wechat.bean.RequestParams;
import xin.pwdkeeper.wechat.service.RedisService;
import xin.pwdkeeper.wechat.toolutil.CommonConstants;
import xin.pwdkeeper.wechat.toolutil.RedisKeysUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 这个controller用于处理web页面
 * 提供web页面使用的API接口
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2025/3/11   14:16
 * @Version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/webFront")
public class WebFrontController {

    @Autowired
    private RedisService redisService;

    /**
     * 校验验证码
     * @param request
     * @return
     */
    @PostMapping(value = "/verifyCode",produces = "application/json;charset=utf-8")
    public R getVerifyCode(@RequestBody RequestParams request) {
        Map<String, Object> data = (Map<String, Object>)request.getRequestBody();
        if (data.get("verifyCode") == null) {
            return R.failed(null, "验证码不能为空");
        }
        Object verifyCode = redisService.get(RedisKeysUtil.VERIFY_CODE_KEY + request.getUserId());
        if (verifyCode == null) {
            return R.failed(null, "验证码已过期");
        }else if(data.get("verifyCode").equals(verifyCode)){
            return R.ok(CommonConstants.SUCCESS);
        }
        return R.failed("验证码错误");
    }

    /**
     * 生成验证码
     * @param request
     * @return
     */
    @PostMapping(value = "/generateVerifyCode", produces = "application/json;charset=utf-8")
    public R generateVerifyCode(@RequestBody RequestParams request) {
        return redisService.generateVerifyCode(request);
    }


}
