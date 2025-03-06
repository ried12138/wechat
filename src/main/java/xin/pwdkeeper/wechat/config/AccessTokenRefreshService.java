package xin.pwdkeeper.wechat.config;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2025/3/6   18:22
 * @Version 1.0
 */
@Service
@EnableScheduling
@Slf4j
public class AccessTokenRefreshService {

    @Autowired
    private WxMpService wxMpService;

    /**
     * 调整刷新间隔为7100秒（1小时58分），预留更多缓冲时间：
     */
    @Scheduled(fixedRate = 7100 * 1000)  //每2小时刷新
    public void refreshToken() {
        try {
            wxMpService.getAccessToken(true);
        } catch (WxErrorException e) {
            log.error("刷新Token失败", e);
        }
    }
}
