package xin.pwdkeeper.wechat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import xin.pwdkeeper.wechat.bean.R;
import xin.pwdkeeper.wechat.service.ChatOPenAIService;
import java.util.Map;

/**
 * 接入人工智能AI
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2025/3/27   15:50
 * @Version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    @Autowired
    private ChatOPenAIService chatOPenAIService;

    /**
     * deepseek R1 接口
     * 流式调用
     * @param message
     * @return
     */
    @GetMapping(value = "/deepSeekR1/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamChat(@RequestParam String message, @RequestParam String openId) {
        log.info("请求聊天时的openId："+openId);
        Flux<String> stringFlux = chatOPenAIService.deepSeekR1streamChat(message,openId);
        log.info("返回数据"+stringFlux);
        return stringFlux;
    }

    /**
     * deepseek R1 接口
     * 同步请求
     * @param request
     * @return
     */
    @PostMapping(value = "/completion", produces = "application/json;charset=utf-8")
    public R getCompletion(@RequestBody Map<String, String> request) {
        return chatOPenAIService.chatCompletion(request.get("message"));
    }
}
