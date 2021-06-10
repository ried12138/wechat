package xyz.taobaok.wechat.toolutil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import xyz.taobaok.wechat.service.UserInfoService;

/**
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2021/6/9   3:44 下午
 * @Version 1.0
 */
@Component
public class AsyncMysqlServiceImpl {

    @Autowired
    UserInfoService userInfoService;


    /**
     *  除淘宝以外
     *  京东 拼多多 可以插入用户的方法
     * @param fromUserName
     * @param specialId
     * @param openId
     */
    @Async
    public void UserCheckInsert(String fromUserName, String specialId, String openId){
        userInfoService.userInfoBind(fromUserName,specialId,openId);
    }
}
