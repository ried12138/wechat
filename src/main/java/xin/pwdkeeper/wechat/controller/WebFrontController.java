package xin.pwdkeeper.wechat.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xin.pwdkeeper.wechat.bean.DictItem;
import xin.pwdkeeper.wechat.bean.R;
import xin.pwdkeeper.wechat.bean.RequestParams;
import xin.pwdkeeper.wechat.customizeService.UserManagementService;
import xin.pwdkeeper.wechat.service.DictItemService;
import xin.pwdkeeper.wechat.service.MinioService;

import java.io.ByteArrayInputStream;
import java.util.Base64;
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
    private MinioService minioService;

//   @Autowired
//   private DictItemService dictItemService;


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
     * 文件上传
     * @param request
     * @return
     */
    @PostMapping("/file/upload")
    @PreAuthorize("isAuthenticated()")
    public R uploadFile(@RequestBody RequestParams request) {
        try {
            return minioService.uploadFile(request);
        } catch (Exception e) {
            return R.failed("上传失败: " + e.getMessage());
        }
    }

    /**
     * 获取图片url
     * @param request
     * @return
     */
    @PostMapping(value = "/file/imageUrl")
    @PreAuthorize("isAuthenticated()")
    public R getImageUrl(@RequestBody RequestParams request) {
        return minioService.getImageUrl(request.getOpenId());
    }

    /**
     * 删除图片
     * @param request
     * @return
     */
    @PostMapping(value = "/file/imageDelete")
    @PreAuthorize("isAuthenticated()")
    public R imageDelete(@RequestBody RequestParams request) {
        return minioService.imageDelete(request.getOpenId());
    }

    /**
     * 通过web请求方式请求，用于通过postman来测试接口使用
     * 请求deepseek API 同步请求
     * 测试地址，不可在生产环境使用
     */
//    @PostMapping(value = "/getUserInfoData", produces = "application/json;charset=utf-8")
//    public R getUserInfoData(@RequestBody RequestParams request) {
//        Map<String, Object> data = (Map<String, Object>)request.getRequestParam();
//        String text = (String) data.get("text");
//        String platFormText = text.substring(0, text.length() - 2);
//        DictItem dictItem = new DictItem();
//        dictItem.setItemValue(platFormText); @PostMapping(value = "/getUserInfoData", produces = "application/json;charset=utf-8")
//        return dictItemService.addDictItem(dictItem);
//    }
}
