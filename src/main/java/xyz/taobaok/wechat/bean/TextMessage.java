package xyz.taobaok.wechat.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.Map;

/**
 * 文本消息
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2021/2/25   3:13 下午
 * @Version 1.0
 */

@XStreamAlias("xml")
public class TextMessage extends BaseMessage{

    //文本内容
    @XStreamAlias("Content")
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public TextMessage(Map<String, String> requestMap, String content) {
        super(requestMap);
        this.setMsgType("text");
        this.content = content;
    }
}
