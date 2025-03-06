package xin.pwdkeeper.wechat.service.serviceImpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xin.pwdkeeper.wechat.bean.WechatUserInfo;
import xin.pwdkeeper.wechat.mapper.WechatUserInfoMapper;
import xin.pwdkeeper.wechat.service.UserInfoService;

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
     * 此方法目前不可用 2025/3/5
     * 插入用户信息
     * @param fromUserName
     * @param specialId
     * @param openId
     * @return
     */
    @Transactional
    @Override
    public int userInfoBind(String fromUserName, String specialId, String openId) {
        WechatUserInfo wechatUserInfo = wechatUserInfoMapper.selectBySpecialId(fromUserName);
        if (wechatUserInfo == null){
            WechatUserInfo user = new WechatUserInfo(fromUserName,openId,specialId);
            user.setUpdateTime(new Date());
            int i = wechatUserInfoMapper.insertSelective(user);
//            UserWallet userWallet = new UserWallet();
//            userWallet.setOpenId(openId);
//            int is = userWalletService.insertSelective(userWallet);
//            if ((i+is)==2){
//                return 1;
//            }
            return 0;
        }else if(wechatUserInfo.getSpecialId() == null){
            wechatUserInfo.setSpecialId(specialId);
            wechatUserInfo.setUpdateTime(new Date());
            return wechatUserInfoMapper.updateByPrimaryKeySelective(wechatUserInfo);
        }else{
            log.info("微信用户已存在：{}",fromUserName);
            return 0;
        }
    }

    @Override
    public WechatUserInfo queryUserInfo(String fromUserName) {
        return wechatUserInfoMapper.selectBySpecialFromUserName(fromUserName);
    }
}
