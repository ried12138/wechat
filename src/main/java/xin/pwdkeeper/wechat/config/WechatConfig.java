package xin.pwdkeeper.wechat.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.WxMpConfigStorage;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2025/3/6   16:05
 * @Version 1.0
 */
@Configuration
@EnableScheduling
@Slf4j
@ConfigurationProperties(prefix = "wx.mp")
@Data
public class WechatConfig {

    private String appId;
    private String secret;
    private String token;

    @Bean
    public WxMpConfigStorage wxMpConfigStorage() {
        WxMpDefaultConfigImpl config = new WxMpDefaultConfigImpl();
        config.setAppId(appId);
        config.setSecret(secret);
        config.setToken(token);
        return config;
    }
}