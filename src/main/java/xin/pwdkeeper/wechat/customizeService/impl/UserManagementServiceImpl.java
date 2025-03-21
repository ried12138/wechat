package xin.pwdkeeper.wechat.customizeService.impl;

import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import xin.pwdkeeper.wechat.bean.AccountInfo;
import xin.pwdkeeper.wechat.bean.R;
import xin.pwdkeeper.wechat.bean.RequestParams;
import xin.pwdkeeper.wechat.bean.WechatUserInfo;
import xin.pwdkeeper.wechat.customizeService.UserManagementService;
import xin.pwdkeeper.wechat.service.AccountInfoService;
import xin.pwdkeeper.wechat.service.WechatUserInfoService;
import xin.pwdkeeper.wechat.util.AesUtil;
import xin.pwdkeeper.wechat.util.RedisKeysUtil;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

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


    /**
     * 添加一个用户财产
     *
     * @param request
     * @return
     */
    @Transactional
    @Override
    public R addUserInfoData(RequestParams request) {
        AccountInfo accountInfo = (AccountInfo) request.getRequestParam();
        WechatUserInfo wechatUserInfo = wechatUserInfoService.getWechatUserInfoByUserOpenId(request.getOpenId());
        accountInfo.setUserId(wechatUserInfo.getId());
        accountInfo.setDefaultTimes();
        String password = accountInfo.getPassword();
        if (password != null) {
            try {
                accountInfo.setPassword(AesUtil.encrypt(password));
            } catch (Exception e) {
                R.failed(null, "加密失败,请重试"+e.getMessage());
            }
        }
        return R.ok(accountInfoService.addAccountInfo(accountInfo));
    }

    /**
     * 删除一个/多个用户财产
     *
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
     *
     * @param request
     * @return
     */
    @Override
    @Transactional
    public R alterUserInfoData(RequestParams request) {
        List<AccountInfo> accountInfo = (List<AccountInfo>) request.getRequestParam();
        return R.ok(accountInfoService.updateAccountInfo(accountInfo));
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
            return R.ok("安全退出");
        }
        return R.failed(null, "出现意外，没有安全的退出系统，请重试");
    }

    /**
     * 财产解密
     * @param request
     * @return
     */
    @Override
    public R getDecryptDate(RequestParams request) {
        AccountInfo accountInfo = (AccountInfo) request.getRequestParam();
        String password = accountInfo.getPassword();
        if (password != null) {
            try {
                accountInfo.setPassword(AesUtil.decrypt(password));
            } catch (Exception e) {
                return R.failed(null, "解密失败,请重试");
            }
            return R.ok(accountInfo);
        }
        return R.failed(null, "没有密码");
    }
}
