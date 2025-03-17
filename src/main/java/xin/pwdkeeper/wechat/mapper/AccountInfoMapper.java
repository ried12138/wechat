package xin.pwdkeeper.wechat.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xin.pwdkeeper.wechat.bean.AccountInfo;

import java.util.List;

@Mapper
public interface AccountInfoMapper {
    int insert(AccountInfo accountInfo);
    AccountInfo selectById(int id);
    List<AccountInfo> selectAll();
    int update(List<AccountInfo> accountInfo);
    int bulkChangesFlagState(List<Integer> ids);
    List<AccountInfo> selectByUserId(AccountInfo accountInfo);
    void delete(int id);
}