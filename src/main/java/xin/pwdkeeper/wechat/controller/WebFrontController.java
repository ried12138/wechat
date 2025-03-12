package xin.pwdkeeper.wechat.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xin.pwdkeeper.wechat.bean.R;
import xin.pwdkeeper.wechat.bean.RequestParams;
import xin.pwdkeeper.wechat.service.VerifyCodeService;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
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
    private VerifyCodeService verifyCodeService;

    /**
     * 校验验证码
     * 生产环境中不需要这个接口提供服务，
     * 这里只是对验证码进行校验的逻辑，
     * 后续可能要删除此接口
     * @param request
     * @return
     */
    @PostMapping(value = "/verifyCode",produces = "application/json;charset=utf-8")
    @PreAuthorize("isAuthenticated()")
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

    /**
     * 认证后的POST请求示例
     * @return
     */
//    @PostMapping(value = "/authenticatedEndpoint", produces = "application/json;charset=utf-8")
//    @PreAuthorize("isAuthenticated()")
//    public R authenticatedEndpoint(@RequestBody RequestParams request) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String username = authentication.getName();
//        Map<String, String> stringStringHashMap = new HashMap<>();
//        stringStringHashMap.put("username",username);
//        return R.ok(stringStringHashMap);
//    }
}
