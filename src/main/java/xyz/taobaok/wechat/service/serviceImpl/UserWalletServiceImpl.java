package xyz.taobaok.wechat.service.serviceImpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.taobaok.wechat.bean.UserWallet;
import xyz.taobaok.wechat.mapper.UserWalletMapper;
import xyz.taobaok.wechat.service.UserWalletService;

/**
 * 钱包服务逻辑
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2021/4/23   7:32 下午
 * @Version 1.0
 */
@Slf4j
@Service
public class UserWalletServiceImpl implements UserWalletService {

    @Autowired
    UserWalletMapper userWalletMapper;

    @Override
    public int insertSelective(UserWallet userWallet) {
        return userWalletMapper.insertSelective(userWallet);
    }

    @Override
    public UserWallet queryUserWalletInfo(String fromUserName) {
        return userWalletMapper.queryUserWalletInfo(fromUserName);
    }
}
