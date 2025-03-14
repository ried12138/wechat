package xin.pwdkeeper.wechat.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xin.pwdkeeper.wechat.bean.R;
import xin.pwdkeeper.wechat.bean.RequestParams;
import xin.pwdkeeper.wechat.customizeService.UserManagementService;


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

    @Autowired
    private UserManagementService userManagementService;

    /**
     * 添加用户信息数据
     * @return
     */
    @PostMapping(value = "/webAddUserInfoData", produces = "application/json;charset=utf-8")
    @PreAuthorize("isAuthenticated()")
    public R addUserInfoData(@RequestBody RequestParams request) {return userManagementService.addUserInfoData(request);}

    /**
     * 批量移除用户信息，资产数据
     * @param request
     * @return
     */
    @PostMapping(value = "/webRemoveUserInfoData", produces = "application/json;charset=utf-8")
    @PreAuthorize("isAuthenticated()")
    public R removeUserInfoData(@RequestBody RequestParams request) {return userManagementService.removeUserInfoData(request);}

    /**
     * 修改用户信息，资产数据
     * @param request
     * @return
     */
    @PostMapping(value = "/webAlterUserInfoData", produces = "application/json;charset=utf-8")
    @PreAuthorize("isAuthenticated()")
    public R webAlterUserInfoData(@RequestBody RequestParams request) {return userManagementService.alterUserInfoData(request);}

    /**
     * 获取用户信息，资产数据
     * @param request
     * @return
     */
    @PostMapping(value = "/webFetchUserInfoData", produces = "application/json;charset=utf-8")
    @PreAuthorize("isAuthenticated()")
    public R webFetchUserInfoData(@RequestBody RequestParams request) {return userManagementService.fetchUserInfoDataPage(request);}

}
