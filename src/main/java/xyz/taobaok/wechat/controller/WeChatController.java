package xyz.taobaok.wechat.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.taobaok.wechat.service.WeChatService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 微信接口管理服务
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2021/2/24   2:59 下午
 * @Version 1.0
 */
@Slf4j
@RestController
public class WeChatController {


    @Autowired
    WeChatService weChatService;

    /**
     * 服务器与微信公众号校验token值
     * @param signature  微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。
     * @param timestamp  时间戳
     * @param nonce      随机数
     * @param echostr    随机字符串
     * @return
     */
    @RequestMapping(value = "/wechat",method = RequestMethod.GET,produces = "text/html;charset=utf-8")
    @ResponseBody
    public String weChatCheck(@RequestParam("signature") String signature, @RequestParam("timestamp") String timestamp,
                            @RequestParam("nonce") String nonce, @RequestParam("echostr") String echostr){
        log.info("signature:{},timestamp:{},nonce:{},echostr:{}",signature,timestamp,nonce,echostr);
        return weChatService.webChatCheck(signature,timestamp,nonce,echostr);
    }


    @RequestMapping(value = "/wechat",method = RequestMethod.POST)
    @ResponseBody
    public String RequestPostweChat(HttpServletRequest request, HttpServletResponse response){
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/xml");
        return weChatService.webChatRequestParse(request);
    }

    @RequestMapping(value = "/",method = RequestMethod.GET,produces = "text/html;charset=utf-8")
    @ResponseBody
    public String index(){
        return "is ok";
    }
//    @RequestMapping(value = "/well-known/pki-validation/fileauth.txt",method = RequestMethod.GET,produces = "text/html;charset=utf-8")
//    @ResponseBody
//    public String checkssl(){
//        return "202104140553224ihvjutr4eccyctmlwcj96elaynjzd4e4hsuqnd9nmcatrv2cz";
//    }
}
