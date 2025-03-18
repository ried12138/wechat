package xin.pwdkeeper.wechat.customizeService.impl;

import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import xin.pwdkeeper.wechat.bean.AccountInfo;
import xin.pwdkeeper.wechat.bean.R;
import xin.pwdkeeper.wechat.bean.RequestParams;
import xin.pwdkeeper.wechat.bean.WechatUserInfo;
import xin.pwdkeeper.wechat.customizeService.UserManagementService;
import xin.pwdkeeper.wechat.service.AccountInfoService;
import xin.pwdkeeper.wechat.service.WechatUserInfoService;

import java.util.List;

/**
 * 用于处理账号事务服务
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

    /**
     * 添加一个用户财产
     * @param request
     * @return
     */
    @Transactional
    @Override
    public R addUserInfoData(RequestParams request) {
        AccountInfo accountInfo = (AccountInfo)request.getRequestParam();
        WechatUserInfo wechatUserInfo = wechatUserInfoService.getWechatUserInfoByUserOpenId(request.getOpenId());
        accountInfo.setUserId(wechatUserInfo.getId());
        accountInfo.setDefaultTimes();
        return R.ok(accountInfoService.addAccountInfo(accountInfo));
    }

    /**
     * 删除一个/多个用户财产
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
}
