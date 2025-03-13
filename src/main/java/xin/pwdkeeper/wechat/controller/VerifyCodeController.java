package xin.pwdkeeper.wechat.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xin.pwdkeeper.wechat.bean.R;
import xin.pwdkeeper.wechat.bean.RequestParams;
import xin.pwdkeeper.wechat.customizeService.VerifyCodeService;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

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
@RequestMapping("/verifyCode")
public class VerifyCodeController {

    @Autowired
    private VerifyCodeService verifyCodeService;

    /**
     * 校验验证码
     * 生产环境中不需要这个接口提供服务，
     * 这里只是对验证码进行校验的逻辑，
     * 后续可能要删除此接口
     * @param request
     * @return
     */
    @PostMapping(value = "/verificationCode",produces = "application/json;charset=utf-8")
    public R getVerifyCode(@RequestBody RequestParams request) {
        return verifyCodeService.parseVerifyCodeService(request);
    }

    /**
     * 生成验证码
     * @param request
     * @return
     */
    @PostMapping(value = "/generateVerifyCode", produces = "application/json;charset=utf-8")
    public R generateVerifyCode(@RequestBody RequestParams request) throws NoSuchPaddingException, IllegalBlockSizeException, UnsupportedEncodingException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        return verifyCodeService.generateVerifyCode(request);
    }


}
