package xin.pwdkeeper.wechat.service;

import xin.pwdkeeper.wechat.bean.AccountInfo;

import java.util.List;

public interface AccountInfoService {
    void addAccountInfo(AccountInfo accountInfo);
    AccountInfo getAccountInfoById(int id);
    List<AccountInfo> getAllAccountInfos();
    void updateAccountInfo(AccountInfo accountInfo);
    void deleteAccountInfo(int id);
}