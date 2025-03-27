package xin.pwdkeeper.wechat.service;

import reactor.core.publisher.Flux;

/**
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2025/3/27   15:55
 * @Version 1.0
 */
public interface ChatOPenAIService {

    Flux<String> deepSeekR1streamChat(String message);
}
