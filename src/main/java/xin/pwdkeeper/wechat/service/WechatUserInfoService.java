package xin.pwdkeeper.wechat.service;

import xin.pwdkeeper.wechat.bean.WechatUserInfo;

import java.util.List;

public interface WechatUserInfoService {
    void addWechatUserInfo(WechatUserInfo wechatUserInfo);
    WechatUserInfo getWechatUserInfoById(int id);
    List<WechatUserInfo> getAllWechatUserInfos();
    void updateWechatUserInfo(WechatUserInfo wechatUserInfo);
    void deleteWechatUserInfo(int id);
}