package xyz.taobaok.wechat.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2021/2/25   11:03 下午
 * @Version 1.0
 */
@Data
@Configuration
public class DtkManager {

    @Value("${dtk.getPrivilegeLink}")
    public String getPrivilegeLink;
    @Value("${dtk.details}")
    public String details;
    @Value("${dtk.appKey}")
    public String appKey;
    @Value("${dtk.appSecret}")
    public String appSecret;
    @Value("${dtk.pid}")
    public String pid;
    @Value("${dtk.dataokeToken}")
    public String dataokeToken;
    @Value("${dtk.jdItemConvert}")
    public String jdItemConvert;
    @Value("${dtk.tklConvert}")
    public String tklConvert;
    @Value("${dtk.orderDetails}")
    public String orderDetails;
    @Value("${dtk.creatTkl}")
    public String creatTkl;



}
