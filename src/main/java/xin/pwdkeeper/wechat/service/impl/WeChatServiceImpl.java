package xin.pwdkeeper.wechat.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xin.pwdkeeper.wechat.bean.BaseMessage;
import xin.pwdkeeper.wechat.bean.TextMessage;
import xin.pwdkeeper.wechat.util.WechatMessageUtil;
import xin.pwdkeeper.wechat.service.WeChatService;

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

    private static final String ISEMY = "抱歉,该商品没有优惠券！";
    private static final String TEXTERROR = "请输入正确的商品链接或者淘口令！\n目前支持淘宝、天猫、京东商品优惠信息";


    @Autowired
    WeChatParseEvent weChatParseEvent;
//    @Autowired
//    WeChatParseMessage weChatParseMessage;


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
        log.info("wechat token校验接口被调用");
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
     * 目前接口无法直接使用 2025.3.5
     * 三大平台的商品转链服务
     * 处理微信互动消息
     * 目前只支持消息回复和关注处理
     * @return
     */
    @Override
    public String webChatRequestParse(HttpServletRequest request) {
        BaseMessage msg = null;
        String content = WechatMessageUtil.menuText();
        Map<String, String> requestMap = WechatMessageUtil.parseXml(request);
        //获取微信用户信息
        String fromUserName = requestMap.get("FromUserName");
        log.info("被请求了！！！！！！请求信息为：{}",requestMap.toString());
        switch (requestMap.get("MsgType")){
            case WechatMessageUtil.RESP_MESSAGE_TYPE_TEXT:  //文本
                String str = requestMap.get("Content");
//                Map<String, String> parse = UrlUtil.parse(str);
//                String platform = parse.get("platform");
//                parse.put("FromUserName",fromUserName);
//                if (platform != null){
//                    switch (platform){
//
//                    }
//                }else{
                    //淘口令
//                    String tpwd = TpwdUtil.isTpwd(parse.get("url"));
//                    if (tpwd != null){
//                        content = getTklConvert(tpwd,fromUserName);
//                        content = content == null? ISEMY:content;
//                    }else{
//                        msg = new TextMessage(requestMap, TEXTERROR);
//                    }
//                }
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
                    default:
                        break;
                }
                break;
            default:
                break;
        }
        if(msg == null){
            msg = new TextMessage(requestMap, content);
        }
        return "WechatMessageUtil.beanToXml(msg)";
    }
}
