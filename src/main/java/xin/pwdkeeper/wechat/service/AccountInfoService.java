package xin.pwdkeeper.wechat.service;

import com.github.pagehelper.PageInfo;
import xin.pwdkeeper.wechat.bean.AccountInfo;

import java.util.List;

public interface AccountInfoService {
    int addAccountInfo(AccountInfo accountInfo);
    AccountInfo getAccountInfoById(int id);
    List<AccountInfo> getAllAccountInfos();
    int updateAccountInfo(List<AccountInfo> accountInfo);
    void deleteAccountInfo(int id);
    int removeTheMarkerAccountInfo(List<Integer> id);
    PageInfo<AccountInfo> getAccountInByUserIdWithPagination(AccountInfo accountInfo);
}