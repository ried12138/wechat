package xyz.taobaok.wechat.service;

import xyz.taobaok.wechat.bean.UserWallet;

/**
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2021/4/23   7:32 下午
 * @Version 1.0
 */
public interface UserWalletService {

    int insertSelective(UserWallet userWallet);

    UserWallet queryUserWalletInfo(String fromUserName);

}
