package xin.pwdkeeper.wechat.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xin.pwdkeeper.wechat.bean.R;
import xin.pwdkeeper.wechat.bean.RequestParams;
import xin.pwdkeeper.wechat.customizeService.LicenseService;

/**
 * 学习码(防盗版授权)对外接口
 * 供请求方软件调用：先用微信获取的验证码换取加密凭证，之后每11分钟携带凭证进行心跳校验。
 *
 * @Author weiranliu
 * @Version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/license")
public class LicenseController {

    @Autowired
    private LicenseService licenseService;

    /**
     * 校验学习验证码，校验通过返回加密授权凭证
     * 请求体：requestParam = { "licenseCode": "XXM123456" }
     */
    @PostMapping(value = "/verify", produces = "application/json;charset=utf-8")
    public R verify(@RequestBody RequestParams request) {
        log.info("[学习码-校验] 收到校验请求, openId={}, requestParam={}", request.getOpenId(), request.getRequestParam());
        R result = licenseService.verifyLicenseCode(request);
        log.info("[学习码-校验] 处理完成, openId={}, code={}, msg={}", request.getOpenId(), result.getCode(), result.getMsg());
        return result;
    }

    /**
     * 心跳校验：携带上一次的加密凭证，返回轮转后的新凭证
     * 请求体：requestParam = { "licenseToken": "<上一次返回的加密凭证>" }
     */
    @PostMapping(value = "/heartbeat", produces = "application/json;charset=utf-8")
    public R heartbeat(@RequestBody RequestParams request) {
        log.info("[学习码-心跳] 收到心跳请求, openId={}", request.getOpenId());
        R result = licenseService.heartbeatLicense(request);
        log.info("[学习码-心跳] 处理完成, openId={}, code={}, msg={}", request.getOpenId(), result.getCode(), result.getMsg());
        return result;
    }
}
