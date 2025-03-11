package xin.pwdkeeper.wechat.mapper;

import org.apache.ibatis.annotations.Mapper;
import xin.pwdkeeper.wechat.bean.AccountInfo;

import java.util.List;

@Mapper
public interface AccountInfoMapper {
    void insert(AccountInfo accountInfo);
    AccountInfo selectById(int id);
    List<AccountInfo> selectAll();
    void update(AccountInfo accountInfo);
    void delete(int id);
}