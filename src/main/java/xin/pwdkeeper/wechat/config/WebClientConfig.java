package xin.pwdkeeper.wechat.config;

import io.netty.channel.ChannelOption;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

/**
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2025/3/27   16:08
 * @Version 1.0
 */
@Configuration
public class WebClientConfig {

    @Value("${deepseek.apiKey}")
    private String APIkey;

    @Value("${deepseek.base_url}")
    private String baseUrl;

    HttpClient httpClient = HttpClient.create()
            .responseTimeout(Duration.ofSeconds(15))    // 响应超时
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000); // 连接超时


    // WebClient 配置
    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + APIkey)
                .build();
    }
}
