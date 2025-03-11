package xin.pwdkeeper.wechat.mapper;

import org.apache.ibatis.annotations.Mapper;
import xin.pwdkeeper.wechat.bean.WechatUserInfo;

import java.util.List;

@Mapper
public interface WechatUserInfoMapper {
    void insert(WechatUserInfo wechatUserInfo);
    WechatUserInfo selectById(int id);
    List<WechatUserInfo> selectAll();
    void update(WechatUserInfo wechatUserInfo);
    void delete(int id);
}