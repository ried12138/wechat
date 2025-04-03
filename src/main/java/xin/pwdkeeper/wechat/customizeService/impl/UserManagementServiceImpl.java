package xin.pwdkeeper.wechat.customizeService.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageInfo;
import io.minio.errors.*;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import xin.pwdkeeper.wechat.bean.*;
import xin.pwdkeeper.wechat.customizeService.UserManagementService;
import xin.pwdkeeper.wechat.service.AccountInfoService;
import xin.pwdkeeper.wechat.service.MinioService;
import xin.pwdkeeper.wechat.service.WechatUserInfoService;
import xin.pwdkeeper.wechat.util.AesUtil;
import xin.pwdkeeper.wechat.util.RedisKeysUtil;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

/**
 * 用于处理账号事务服务
 *
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2025/3/13   18:43
 * @Version 1.0
 */
@Slf4j
@Service
public class UserManagementServiceImpl implements UserManagementService {

    @Autowired
    private WechatUserInfoService wechatUserInfoService;

    @Autowired
    private AccountInfoService accountInfoService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private MinioService minioService;

    /**
     * 添加一个用户财产
     *
     * @param request
     * @return
     */
    @Transactional
    @Override
    public R addUserInfoData(RequestParams request) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        AccountInfo accountInfo = (AccountInfo) request.getRequestParam();
        WechatUserInfo wechatUserInfo = wechatUserInfoService.getWechatUserInfoByUserOpenId(request.getOpenId());
        accountInfo.setUserId(wechatUserInfo.getId());
        accountInfo.setDefaultTimes();
        String password = accountInfo.getPassword();
        if (password != null) {
            try {
                accountInfo.setPassword(AesUtil.encrypt(password));
            } catch (Exception e) {
                R.failed(null, "加密失败,请重试" + e.getMessage());
            }
        }
        int count = accountInfoService.addAccountInfo(accountInfo);
        if (count >= 1) {
            log.info("数据变动：account_info表中有" + 1 + "条数据被插入");
            //上传图片
            R r = minioService.uploadFile(request);
            if (r.getCode() !=0){
                throw new RuntimeException("上传文件时发生错误");
            }
            return R.ok("添加成功");
        }
        return R.failed(null, "上传文件时发生错误");
    }

    /**
     * 删除一个/多个用户财产
     * 不删除附件
     * @param request
     * @return
     */
    @Override
    @Transactional
    public R removeUserInfoData(RequestParams request) {
        List<Integer> ids = (List<Integer>) request.getRequestParam();
        return R.ok(accountInfoService.removeTheMarkerAccountInfo(ids));
    }

    /**
     * 修改一个用户财产
     * 目前服务支持一个单个数据的修改
     * @param request
     * @return
     */
    @Override
    @Transactional
    public R alterUserInfoData(RequestParams request) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        List<Object> accountInfo = (List<Object>) request.getRequestParam();
        if (accountInfoService.updateAccountInfo(accountInfo) >= 1){
            log.info("数据变动：account_info表中有"+accountInfo.size()+"条数据被修改");
            //是否有图片上传
            ObjectMapper mapper = new ObjectMapper();
            for (Object accountInfoBean : accountInfo) {
                AccountInfo info = mapper.convertValue(accountInfoBean, AccountInfo.class);
                FileRequestBean fileRequestBean = info.getFileRequestBean();
                if (fileRequestBean != null){
                    RequestParams requestParams = new RequestParams();
                    requestParams.setOpenId(request.getOpenId());
                    requestParams.setRequestParam(info);
                    R r = minioService.uploadFile(requestParams);
                    if (r.getCode() !=0){
                        return r;
                    }
                }
            }
        }
        return R.ok();
    }

    /**
     * 分页获取用户财产
     *
     * @param request
     * @return
     */
    @Override
    public R fetchUserInfoDataPage(RequestParams request) {
        AccountInfo accountInfo = (AccountInfo) request.getRequestParam();
        WechatUserInfo wechatUserInfo = wechatUserInfoService.getWechatUserInfoByUserOpenId(request.getOpenId());
        accountInfo.setUserId(wechatUserInfo.getId());
        PageInfo<AccountInfo> accountInfoPageInfo = accountInfoService.getAccountInByUserIdWithPagination(accountInfo);
        return R.ok(accountInfoPageInfo);
    }


    /**
     * 退出web
     *
     * @param request
     * @return
     */
    @Override
    public R signOut(RequestParams request) {
        if (redisTemplate.delete(RedisKeysUtil.VERIFY_CODE_KEY + request.getOpenId())) {
            return R.ok("账号已经安全关闭并退出");
        }
        return R.failed(null, "出现意外，没有安全的退出系统，请重试");
    }

    /**
     * 财产解密
     *
     * @param request
     * @return
     */
    @Override
    public R getDecryptDate(RequestParams request) {
        Map<String, Object> data = (Map<String, Object>)request.getRequestParam();
        Integer type = (Integer) data.get("type");
        String password = (String)data.get("password");
        if (password == null || password.isEmpty()){
            return R.failed(null, "密码不能为空");
        }
        switch (type){
            //解密密码
            case 1:
                try {
                    String decrypt = AesUtil.decrypt(password);
                    data.put("password", decrypt);
                } catch (Exception e) {
                    return R.failed(null, "解密失败,请重试");
                }
                break;
            //加密密码
            case 2:
                try {
                    String encrypt = AesUtil.encrypt(password);
                    data.put("password", encrypt);
                } catch (Exception e) {
                    return R.failed(null, "解密失败,请重试");
                }
                break;
        }
        return R.ok(data);
    }

    /**
     * 补全用户信息
     * @param request
     * @return
     */
    @Override
    public R updateCompleteUserInfo(RequestParams request) {
        WechatUserInfo wechatUserInfo = (WechatUserInfo) request.getRequestParam();
        if (wechatUserInfoService.updateWechatUserInfoByOpenId(wechatUserInfo) == 1){
            return R.ok();
        }
        return R.failed(null, "补全用户信息失败");
    }

    /**
     * 获取用户基础信息
     * @param request
     * @return
     */
    @Override
    public R getUserInfo(RequestParams request) {
        WechatUserInfo wechatUserInfo = wechatUserInfoService.selectByUserOpenIdBaseInfo(request.getOpenId());
        if (wechatUserInfo != null ){
            return R.ok(wechatUserInfo);
        }
        return R.failed(null, "获取用户信息失败");
    }
}
