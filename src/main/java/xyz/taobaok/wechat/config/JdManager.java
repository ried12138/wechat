package xyz.taobaok.wechat.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * 京东联盟参数
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2021/2/27   4:10 下午
 * @Version 1.0
 */
@Data
@Configuration
public class JdManager {

    @Value("${jd.appKey}")
    public String appKey;
    @Value("${jd.appSecret}")
    public String appSecret;
    @Value("${jd.unionGoodsItemUrl}")
    public String unionGoodsItemUrl;
    @Value("${jd.unionConvertUrl}")
    public String unionConvertUrl;
    @Value("${jd.routerUrl}")
    public String routerUrl;
    @Value("${jd.unionId}")
    public Long unionId;
}
