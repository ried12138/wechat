package xin.pwdkeeper.wechat.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xin.pwdkeeper.wechat.bean.R;
import xin.pwdkeeper.wechat.bean.RequestParams;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2025/3/13   13:47
 * @Version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/webFront")
public class WebFrontController {

    /**
     * 认证后的POST请求示例
     * @return
     */
    @PostMapping(value = "/authenticatedEndpoint", produces = "application/json;charset=utf-8")
    @PreAuthorize("isAuthenticated()")
    public R authenticatedEndpoint(@RequestBody RequestParams request) {
        String userId = request.getUserId();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Map<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("username",username);
        return R.ok(stringStringHashMap);
    }
}
