package xin.pwdkeeper.wechat.service;

import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2021/2/24   6:51 下午
 * @Version 1.0
 */

public interface WeChatService {
    String webChatCheck(String signature, String timestamp, String nonce, String echostr);

    WxMpXmlOutMessage webChatRequestParse(WxMpXmlMessage inMessage);
}
