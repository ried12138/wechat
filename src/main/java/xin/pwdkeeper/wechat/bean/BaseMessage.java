package xin.pwdkeeper.wechat.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;

import java.util.Map;

/**
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2021/2/25   2:51 下午
 * @Version 1.0
 */
@Data
public class BaseMessage {
    @XStreamAlias("ToUserName")
    private String toUserName;    //开发者微信号

    @XStreamAlias("FromUserName")
    private String fromUserName;   //发送方帐号

    @XStreamAlias("CreateTime")
    private String createTime;    //消息创建时间

    @XStreamAlias("MsgType")
    private String msgType;          //定义所有的类型

    public BaseMessage(Map<String,String> requestMap) {
        this.toUserName = requestMap.get("FromUserName");
        this.fromUserName = requestMap.get("ToUserName");
        this.createTime = System.currentTimeMillis()/1000+"";
    }
}
