package xin.pwdkeeper.wechat.bean;

import lombok.Data;

import java.util.List;

/**
 * deepseek请求体
 * 聊天请求体
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2025/3/27   16:07
 * @Version 1.0
 */
@Data
public class ChatRequest {

    private String model = "deepseek-chat";
    private List<Message> messages;
    private boolean stream = false;

    @Data
    public static class Message {
        private String role;
        private String content;

        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }
    }
}
