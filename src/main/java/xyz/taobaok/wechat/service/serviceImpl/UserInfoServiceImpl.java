package xyz.taobaok.wechat.service.serviceImpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.taobaok.wechat.bean.WechatUserInfo;
import xyz.taobaok.wechat.mapper.WechatUserInfoMapper;
import xyz.taobaok.wechat.service.UserInfoService;

import java.util.Date;

/**
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2021/4/22   2:33 下午
 * @Version 1.0
 */
@Service
@Slf4j
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    WechatUserInfoMapper wechatUserInfoMapper;

    /**
     * 插入用户信息
     * @param fromUserName
     * @param specialId
     * @param openId
     * @return
     */
    @Override
    public int userInfoBind(String fromUserName, String specialId, String openId) {
        WechatUserInfo wechatUserInfo = wechatUserInfoMapper.selectBySpecialId(specialId);
        if (wechatUserInfo == null){
            WechatUserInfo user = new WechatUserInfo(fromUserName,openId,specialId);
            user.setUpdateTime(new Date());
            return wechatUserInfoMapper.insertSelective(user);
        }else{
            log.info("微信用户已存在：{}",fromUserName);
            return 0;
        }
    }
}
