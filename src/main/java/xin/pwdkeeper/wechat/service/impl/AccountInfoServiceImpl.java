package xin.pwdkeeper.wechat.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xin.pwdkeeper.wechat.bean.AccountInfo;
import xin.pwdkeeper.wechat.mapper.AccountInfoMapper;
import xin.pwdkeeper.wechat.service.AccountInfoService;

import java.util.List;

@Service
public class AccountInfoServiceImpl implements AccountInfoService {
    @Autowired
    private AccountInfoMapper accountInfoMapper;

    @Override
    public void addAccountInfo(AccountInfo accountInfo) {
        accountInfoMapper.insert(accountInfo);
    }

    @Override
    public AccountInfo getAccountInfoById(int id) {
        return accountInfoMapper.selectById(id);
    }

    @Override
    public List<AccountInfo> getAllAccountInfos() {
        return accountInfoMapper.selectAll();
    }

    @Override
    public void updateAccountInfo(AccountInfo accountInfo) {
        accountInfoMapper.update(accountInfo);
    }

    @Override
    public void deleteAccountInfo(int id) {
        accountInfoMapper.delete(id);
    }
}