package xyz.taobaok.wechat.service.serviceImpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.taobaok.wechat.bean.BaseMessage;
import xyz.taobaok.wechat.bean.TextMessage;
import xyz.taobaok.wechat.service.WeChatService;
import xyz.taobaok.wechat.toolutil.UrlUtil;
import xyz.taobaok.wechat.toolutil.WechatMessageUtil;

import javax.servlet.http.HttpServletRequest;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Map;

/**
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2021/2/24   6:51 下午
 * @Version 1.0
 */
@Service
@Slf4j
public class WeChatServiceImpl implements WeChatService {



    @Autowired
    WeChatParseEvent weChatParseEvent;


    /**
     * 服务器与微信公众号校验token值
     * @param signature  微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。
     * @param timestamp  时间戳
     * @param nonce      随机数
     * @param echostr    随机字符串
     * @return
     */
    @Override
    public String webChatCheck(String signature, String timestamp, String nonce, String echostr) {
        //字典顺序排序
        String[] attr = {WechatMessageUtil.TOKEN, timestamp, nonce};
        Arrays.sort(attr);
        StringBuffer content = new StringBuffer();
        for (int i = 0; i < attr.length; i++) {
            content.append(attr[i]);
        }
        //sha1加密
        MessageDigest md = null;
        String temp = null;
        try {
            md = MessageDigest.getInstance("SHA-1");
            byte[] digest = md.digest(content.toString().getBytes());
            temp = WechatMessageUtil.byteTStr(digest);
            log.info("加密后的token：" + temp);
            //小写对比
            if ((temp.toLowerCase()).equals(signature)) {
                return echostr;
            } else {
                return "";
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 三大平台的商品转链服务
     * 处理微信互动消息
     * 目前只支持消息回复和关注处理
     * @return
     */
    @Override
    public String webChatRequestParse(HttpServletRequest request) {
        BaseMessage msg = null;
        Map<String, String> requestMap = WechatMessageUtil.parseXml(request);
        switch (requestMap.get("MsgType")){
            case WechatMessageUtil.RESP_MESSAGE_TYPE_TEXT:  //文本
                String requestContent = requestMap.get("Content");
                Map<String, String> parse = UrlUtil.parse(requestContent);
                if (parse.size() >= 1){
                    switch (parse.get("platform")){
                        case "tb":
                            String itemId = parse.get("id");
                            if (!itemId.isEmpty()){

                            }
                            break;
                        case "jd":
                            break;
                        case "pdd":
                            break;
                    }
                }
                break;
            case WechatMessageUtil.RESP_MESSAGE_TYPE_LINK:  //链接
//                String url = requestMap.get("Url");
//                String description = requestMap.get("Description");
//                log.info("url"+url+"\n description"+description);
//                System.out.println("url"+url+"\n description"+description);
                break;
            case WechatMessageUtil.REQ_MESSAGE_TYPE_EVENT:
                switch (requestMap.get("Event")){
                    case WechatMessageUtil.EVENT_TYPE_SUBSCRIBE:
                        msg = new TextMessage(requestMap, WechatMessageUtil.menuText());
                        break;
                }
                break;
        }
        return WechatMessageUtil.beanToXml(msg);
    }
}
