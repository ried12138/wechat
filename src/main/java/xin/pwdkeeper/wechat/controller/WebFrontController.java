package xin.pwdkeeper.wechat.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xin.pwdkeeper.wechat.bean.DictItem;
import xin.pwdkeeper.wechat.bean.R;
import xin.pwdkeeper.wechat.bean.RequestParams;
import xin.pwdkeeper.wechat.customizeService.UserManagementService;
import xin.pwdkeeper.wechat.service.DictItemService;

import java.util.Map;


/**
 * web页面请求控制器
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

@Autowired
DictItemService dictItemService;


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

    /**
     * 登出，退出账号
     * @param request
     * @return
     */
    @PostMapping(value = "/signOut", produces = "application/json;charset=utf-8")
    @PreAuthorize("isAuthenticated()")
    public R webSignOut(@RequestBody RequestParams request) {return userManagementService.signOut(request);}

    /**
     * 用户资产加/解密
     * @param request
     * @return
     */
    @PostMapping(value = "/decryptDate", produces = "application/json;charset=utf-8")
    @PreAuthorize("isAuthenticated()")
    public R decryptDate(@RequestBody RequestParams request) {return userManagementService.getDecryptDate(request);}

    /**
     * 补全用户信息
     * @param request
     * @return
     */
    @PostMapping(value = "/completeUserInfo", produces = "application/json;charset=utf-8")
    @PreAuthorize("isAuthenticated()")
    public R completeUserInfo(@RequestBody RequestParams request) {return userManagementService.updateCompleteUserInfo(request);}


    /**
     * 获取用户基础信息
     * @param request
     * @return
     */
    @PostMapping(value = "/getUserInfo", produces = "application/json;charset=utf-8")
    @PreAuthorize("isAuthenticated()")
    public R getUserInfo(@RequestBody RequestParams request) {return userManagementService.getUserInfo(request);}


    /**
     * 测试地址，不可在生产环境使用
     */
//    @PostMapping(value = "/getUserInfoData", produces = "application/json;charset=utf-8")
//    public R getUserInfoData(@RequestBody RequestParams request) {
//        Map<String, Object> data = (Map<String, Object>)request.getRequestParam();
//        String text = (String) data.get("text");
//        String platFormText = text.substring(0, text.length() - 2);
//        DictItem dictItem = new DictItem();
//        dictItem.setItemValue(platFormText);
//        return dictItemService.addDictItem(dictItem);
//    }
}
