package xyz.taobaok.wechat.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 淘宝联盟参数
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2021/4/15   5:35 下午
 * @Version 1.0
 */
@Data
@Configuration
public class TbManager {

    @Value("${tb.appKey}")
    private String appKey;
    @Value("${tb.secret}")
    private String secret;
    @Value("${tb.url}")
    private String url;

}
