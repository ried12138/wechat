package xin.pwdkeeper.wechat.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2025/3/12   14:16
 * @Version 1.0
 */
@Slf4j
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${app.cors.enabled}")
    private boolean corsEnabled;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        if (corsEnabled) {
            registry.addMapping("/**") // 修改为允许所有路径
                    .allowedOrigins("http://localhost:8081") // 修改为允许特定来源
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                    .allowedHeaders("*")
                    .allowCredentials(true)
                    .maxAge(3600); // 添加maxAge以避免频繁预检请求
            log.info("目前为开发环境，跨域请求问题已解决:::::::::::::CORS enabled,");
        }
    }
}