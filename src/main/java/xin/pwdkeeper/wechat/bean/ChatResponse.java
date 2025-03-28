package xin.pwdkeeper.wechat.bean;

import lombok.Data;

import java.util.List;

/**
 * deepseek 响应体
 * 返回结果接收
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2025/3/28   17:13
 * @Version 1.0
 */
@Data
public class ChatResponse {

    private String id;
    private List<Choice> choices;

    @Data
    public static class Choice {
        private Message message;
    }

    @Data
    public static class Message {
        private String role;
        private String content;
    }
}
