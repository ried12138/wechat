package xyz.taobaok.wechat.mapper;

import org.springframework.stereotype.Repository;
import xyz.taobaok.wechat.bean.WechatUserInfo;

@Repository
public interface WechatUserInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(WechatUserInfo record);

    int insertSelective(WechatUserInfo record);

    WechatUserInfo selectByPrimaryKey(Integer id);

    WechatUserInfo selectBySpecialId(String specialId);

    int updateByPrimaryKeySelective(WechatUserInfo record);

    int updateByPrimaryKey(WechatUserInfo record);

    WechatUserInfo selectBySpecialFromUserName(String fromUserName);
}