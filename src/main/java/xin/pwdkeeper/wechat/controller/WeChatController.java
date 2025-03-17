package xin.pwdkeeper.wechat.controller;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xin.pwdkeeper.wechat.service.WeChatService;

/**
 * 微信接口管理服务
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2021/2/24   2:59 下午
 * @Version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/wechat")
public class WeChatController {

    @Autowired
    private WxMpService wxService;

    @Autowired
    private WeChatService weChatService;
    /**
     * 服务器与微信公众号校验token值
     *
     * @param signature  微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。
     * @param timestamp  时间戳
     * @param nonce      随机数
     * @param echostr    随机字符串
     * @return
     */
    @GetMapping(value = "/analyInfo",produces = "text/plain;charset=utf-8")
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
    @PostMapping(value = "/analyInfo",produces = "application/xml; charset=UTF-8")
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
        WxMpXmlOutMessage outMessage = weChatService.webChatRequestParse(inMessage);
        if (outMessage == null) {
            return "请重试";
        }
        return outMessage.toEncryptedXml(wxService.getWxMpConfigStorage());
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

}