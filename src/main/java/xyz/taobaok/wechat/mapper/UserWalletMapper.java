package xyz.taobaok.wechat.mapper;

import org.springframework.stereotype.Repository;
import xyz.taobaok.wechat.bean.UserWallet;

@Repository
public interface UserWalletMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserWallet record);

    int insertSelective(UserWallet record);

    UserWallet selectByPrimaryKey(Integer id);

    UserWallet queryUserWalletInfo(String fromUserName);

    int updateByPrimaryKeySelective(UserWallet record);

    int updateByPrimaryKey(UserWallet record);
}