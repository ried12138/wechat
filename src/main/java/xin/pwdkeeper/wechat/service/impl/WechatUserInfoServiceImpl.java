package xin.pwdkeeper.wechat.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xin.pwdkeeper.wechat.bean.WechatUserInfo;
import xin.pwdkeeper.wechat.mapper.WechatUserInfoMapper;
import xin.pwdkeeper.wechat.service.WechatUserInfoService;

import java.util.List;

@Service
public class WechatUserInfoServiceImpl implements WechatUserInfoService {
    @Autowired
    private WechatUserInfoMapper wechatUserInfoMapper;

    @Override
    public void addWechatUserInfo(WechatUserInfo wechatUserInfo) {
        wechatUserInfoMapper.insert(wechatUserInfo);
    }

    @Override
    public WechatUserInfo getWechatUserInfoById(int id) {
        return wechatUserInfoMapper.selectById(id);
    }

    @Override
    public List<WechatUserInfo> getAllWechatUserInfos() {
        return wechatUserInfoMapper.selectAll();
    }

    @Override
    public void updateWechatUserInfo(WechatUserInfo wechatUserInfo) {
        wechatUserInfoMapper.update(wechatUserInfo);
    }

    @Override
    public void deleteWechatUserInfo(int id) {
        wechatUserInfoMapper.delete(id);
    }
    @Override
    public WechatUserInfo getWechatUserInfoByUserOpenId(String userOpenId) {return wechatUserInfoMapper.selectByUserOpenId(userOpenId);}
    @Override
    public boolean isByUserOpenId(String userOpenId){
        WechatUserInfo wechatUserInfo = wechatUserInfoMapper.selectByUserOpenId(userOpenId);
        if (wechatUserInfo != null){
            return true;
        }
        return false;
    }
}