package xyz.taobaok.wechat.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2021/3/2   7:37 下午
 * @Version 1.0
 */
@Data
@Configuration
public class PddManager {
    @Value("${pdd.appKey}")
    public String appKey;
    @Value("${pdd.appSecret}")
    public String appSecret;
    @Value("${pdd.pid}")
    public String pid;
}
