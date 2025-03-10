package xin.pwdkeeper.wechat.controller;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static me.chanjar.weixin.common.api.WxConsts.EventType.SUBSCRIBE;
import static me.chanjar.weixin.common.api.WxConsts.EventType.UNSUBSCRIBE;
import static me.chanjar.weixin.common.api.WxConsts.XmlMsgType.EVENT;
import static me.chanjar.weixin.common.api.WxConsts.XmlMsgType.TEXT;

/**
 * 微信接口管理服务
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2021/2/24   2:59 下午
 * @Version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/")
public class WeChatController {

    @Autowired
    private WxMpService wxService;

    /**
     * 服务器与微信公众号校验token值
     * @param signature  微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。
     * @param timestamp  时间戳
     * @param nonce      随机数
     * @param echostr    随机字符串
     * @return
     */
    @GetMapping(value = "wechat",produces = "text/plain;charset=utf-8")
    public String checkSignature(@RequestParam(name = "signature") String signature,
                                 @RequestParam(name = "timestamp") String timestamp,
                                 @RequestParam(name = "nonce") String nonce,
                                 @RequestParam(name = "echostr") String echostr) {
        if (wxService.checkSignature(timestamp, nonce, signature)) {
            return echostr;
        }
        return "非法请求";
    }

    /**
     * 微信用户信息处理接口
     * @param requestBody
     * @param signature
     * @param timestamp
     * @param nonce
     * @param encType
     * @param msgSignature
     * @return
     */
    @PostMapping(value = "wechat",produces = "application/xml; charset=UTF-8")
    @ResponseBody
    public String handleMessage(@RequestBody String requestBody,
                                @RequestParam("signature") String signature,
                                @RequestParam("timestamp") String timestamp,
                                @RequestParam("nonce") String nonce,
                                @RequestParam(name = "encrypt_type", required = false) String encType,
                                @RequestParam(name = "msg_signature", required = false) String msgSignature) {

        // 消息解密处理
        WxMpXmlMessage inMessage = WxMpXmlMessage.fromEncryptedXml(
                requestBody, wxService.getWxMpConfigStorage(),
                timestamp, nonce, msgSignature);

        // 消息处理逻辑
        WxMpXmlOutMessage outMessage = this.route(inMessage);
        if (outMessage == null) {
            return "";
        }
        return outMessage.toEncryptedXml(wxService.getWxMpConfigStorage());
    }

    private WxMpXmlOutMessage route(WxMpXmlMessage message) {
        try {
            switch (message.getMsgType()) {
                case TEXT:
                    return WxMpXmlOutMessage.TEXT()
                            .content("收到消息：" + message.getContent())
                            .fromUser(message.getToUser())
                            .toUser(message.getFromUser())
                            .build();
                case EVENT:
                    return handleEvent(message);
                default:
                    return null;
            }
        } catch (Exception e) {
            log.error("消息处理异常", e);
        }
        return null;
    }

    private WxMpXmlOutMessage handleEvent(WxMpXmlMessage message) {
        switch (message.getEvent()) {
            case SUBSCRIBE:
                return WxMpXmlOutMessage.TEXT()
                        .content("感谢关注！" + message.getContent())
                        .fromUser(message.getToUser())
                        .toUser(message.getFromUser())
                        .build();
            case UNSUBSCRIBE:
                log.info("用户{}取消关注", message.getFromUser());
                return null;
            default:
                return null;
        }
    }

    /**
     * 处理微信公众号发送信息接口
     * @param request
     * @param response
     * @return
     */
//    @RequestMapping(value = "wechat",method = RequestMethod.POST)
//    @ResponseBody
//    public String RequestPostweChat(HttpServletRequest request, HttpServletResponse response){
//        response.setCharacterEncoding("UTF-8");
//        response.setContentType("text/xml");
//        return weChatService.webChatRequestParse(request);
//    }

    /**
     * 服务测试地址
     * @return
     */
    @RequestMapping(value = "",method = RequestMethod.GET,produces = "text/html;charset=utf-8")
    @ResponseBody
    public String index(){
        return "is ok";
    }

    /**
     * 新增的codeCheck接口
     * @return 返回接收到的字符串
     */

    @PostMapping(value = "checkCode",produces = "application/json;charset=utf-8")
    public Map<String, Object> checkCode(@RequestBody Map<String, String> request) {
        String code = request.get("code");
        // 这里添加验证码验证逻辑
        boolean isValid = validateCode(code); // 假设有一个验证方法
        Map<String, Object> response = new HashMap<>();
        response.put("success", isValid);
        return response;
    }

    private boolean validateCode(String code) {
        // 验证逻辑
        return "123456".equals(code);
    }
}