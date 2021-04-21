package xyz.taobaok.wechat.service;

import com.taobao.api.ApiException;

/**
 * 淘宝私域用户信息服务
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2021/4/15   4:45 下午
 * @Version 1.0
 */
public interface TaobaoApiService {

    String getAccessToken(String code,String refreshToken) throws ApiException;
}
