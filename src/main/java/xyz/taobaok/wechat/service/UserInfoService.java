package xyz.taobaok.wechat.service;

/**
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2021/4/16   3:43 下午
 * @Version 1.0
 */
public interface UserInfoService {

    int userInfoBind(String fromUserName, String specialId, String openId);
}
