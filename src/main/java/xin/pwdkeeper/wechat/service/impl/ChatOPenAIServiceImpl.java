package xin.pwdkeeper.wechat.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import xin.pwdkeeper.wechat.bean.ChatRequest;
import xin.pwdkeeper.wechat.bean.ChatResponse;
import xin.pwdkeeper.wechat.bean.R;
import xin.pwdkeeper.wechat.service.ChatOPenAIService;
import org.springframework.http.HttpStatus;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2025/3/27   15:55
 * @Version 1.0
 */
// 流式响应处理
@Slf4j
@Service
@RequiredArgsConstructor
public class ChatOPenAIServiceImpl implements ChatOPenAIService {

    @Autowired
    private WebClient webClient;

    @Override
    public Flux<String> deepSeekR1streamChat(String message) {
        ChatRequest request = new ChatRequest();
        request.setStream(true);
        request.setMessages(List.of(
                new ChatRequest.Message("user", message)
        ));
        log.info("接收到消息："+ message);
        return webClient.post()
                .bodyValue(request)
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(String.class)
                .onErrorResume(e -> Flux.just("Error: " + e.getMessage()));
    }


    @Override
    public R chatCompletion(String userMessage) {
        // 构建请求体
        ChatRequest request = new ChatRequest();
        request.setMessages(List.of(
                new ChatRequest.Message("user", userMessage)
        ));
        // 发送请求
        try {
            // 使用 WebClient 发送请求
            ChatResponse response = webClient.post()
//                    .uri("")  // 确保与 baseUrl 拼接后路径正确
                    .bodyValue(request)
                    .retrieve()
                    // 处理错误状态码（4xx/5xx）
                    .onStatus(
                            HttpStatus::isError,
                            clientResponse -> clientResponse
                                    .bodyToMono(String.class)
                                    .flatMap(errorBody -> {
                                        // 记录错误日志
                                        log.error("API 请求失败，状态码：{}，错误信息：{}",
                                                clientResponse.statusCode(),
                                                errorBody);
                                        return Mono.error(new RuntimeException(
                                                "API 返回错误：" + clientResponse.statusCode() + " - " + errorBody
                                        ));
                                    })
                    )
                    // 反序列化响应体
                    .bodyToMono(ChatResponse.class)
                    // 设置超时时间（需与底层 HttpClient 配置协同工作）
                    .timeout(Duration.ofSeconds(30))
                    // 阻塞等待结果
                    .block();

            // 处理响应
            if (response != null &&
                    response.getChoices() != null &&
                    !response.getChoices().isEmpty()) {
                Map<String, Object> map = new HashMap<>();
                map.put("json",response.getChoices().get(0).getMessage().getContent());
                return R.ok(map);
            } else {
                return R.failed(null,"分析失败！请重试");
            }
        } catch (RuntimeException e) {
            // 细化异常处理
            if (e.getCause() instanceof TimeoutException) {
                log.error("请求超时", e);
                return R.failed(null,"分析超时！请重试");
            } else if (e.getMessage().contains("API 返回错误")) {
                // 已记录的 API 错误直接抛出
                throw e;
            } else {
                log.error("未知请求错误", e);
                return R.failed(null,"AI服务暂时不可用！请稍后重试");
            }
        }
    }
}
