package xin.pwdkeeper.wechat.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import xin.pwdkeeper.wechat.bean.ChatRequest;
import xin.pwdkeeper.wechat.service.ChatOPenAIService;

import java.util.List;

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
}
