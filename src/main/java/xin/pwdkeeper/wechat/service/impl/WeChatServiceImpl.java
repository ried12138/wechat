package xin.pwdkeeper.wechat.service.impl;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutTextMessage;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xin.pwdkeeper.wechat.bean.*;
import xin.pwdkeeper.wechat.customizeService.UserManagementService;
import xin.pwdkeeper.wechat.customizeService.VerifyCodeService;
import xin.pwdkeeper.wechat.service.WechatUserInfoService;
import xin.pwdkeeper.wechat.service.WeChatService;
import java.util.Date;
import java.util.Map;

import static me.chanjar.weixin.common.api.WxConsts.EventType.SUBSCRIBE;
import static me.chanjar.weixin.common.api.WxConsts.EventType.UNSUBSCRIBE;
import static me.chanjar.weixin.common.api.WxConsts.XmlMsgType.EVENT;
import static me.chanjar.weixin.common.api.WxConsts.XmlMsgType.TEXT;

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
    @Autowired
    private WxMpService wxService;
    @Autowired
    private VerifyCodeService verifyCodeService;
    @Autowired
    private WechatUserInfoService wechatUserInfoService;
    @Autowired
    private UserManagementService userManagementService;

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
        if (wxService.checkSignature(timestamp, nonce, signature)) {
            return echostr;
        }
        return "非法请求";
    }

    /**
     * 目前接口无法直接使用 2025.3.5
     * 三大平台的商品转链服务
     * 处理微信互动消息
     * 目前只支持消息回复和关注处理
     * @return
     */
    @Override
    public WxMpXmlOutMessage webChatRequestParse(WxMpXmlMessage message) {
        WxMpXmlOutTextMessage content = null;
        log.info("被请求了！！！！！！请求信息L用户的openId：{}", message.getFromUser());
        try {
            switch (message.getMsgType()) {
                case TEXT:
                    content = WxMpXmlOutMessage.TEXT()
                            .fromUser(message.getToUser())
                            .toUser(message.getFromUser())
                            .build();
                    //判断消息的内容
                    if (message.getContent().equals("验证码")){
                        RequestParams<Object> requestParam = new RequestParams<>();
                        requestParam.setOpenId(message.getFromUser());
                        R r = verifyCodeService.generateVerifyCode(requestParam);
                        if (r.getCode() != 0 && !r.getMsg().equals("SUCCESS")){
                            content.setContent(r.getMsg());
                            break;
                        }
                        Map<String, Object> data = (Map<String, Object>)r.getData();
                        content.setContent(String.format("获取成功!\n 你的验证码：%s \n点击此处可以查看你的资产 %s", data.get("verifyCode"),data.get("springUrl")));
                        return content;
                    }else if (message.getContent().equals("登记")){
                        message.setEvent(SUBSCRIBE);
                        return handleEvent(message);
                    }else if (message.getContent().equals("退出")){
                        content.setContent(signOut(message));
                        return content;
                    }
                case EVENT:
                    return handleEvent(message);
                default:
                    return content;
            }
        } catch (Exception e) {
            log.error("消息处理异常", e);
        }
        return content;
    }

    private WxMpXmlOutMessage handleEvent(WxMpXmlMessage message) {
        switch (message.getEvent()) {
            case SUBSCRIBE:
                //记录用户信息
                wechatUserInfoService.addWechatUserInfo(new WechatUserInfo(null,message.getFromUser(),1,new Date()));
                return WxMpXmlOutMessage.TEXT()
                        .content("感谢关注！" + message.getContent()+"\n" +
                                "你可以再次登记你的网络虚拟资产\n" +
                                "在对话框中输入【验证码】可以查看你的虚拟账户资产")
                        .fromUser(message.getToUser())
                        .toUser(message.getFromUser())
                        .build();
            case UNSUBSCRIBE:
                log.info("用户{}取消关注", message.getFromUser());
                //查询用户在数据库的状态
                WechatUserInfo wechatUserInfo = wechatUserInfoService.getWechatUserInfoByUserOpenId(message.getFromUser());
                if (wechatUserInfo.getFollowStatus() == 1){
                    wechatUserInfoService.updateWechatUserInfo(new WechatUserInfo(wechatUserInfo.getId(),null,0,null));
                }
                return null;
            default:
                return null;
        }
    }

    /**
     * 安全关闭web连接
     * @param message
     * @return
     */
    private String signOut(WxMpXmlMessage message){
        R r = userManagementService.signOut(new RequestParams<>(message.getFromUser()));
        return r.getMsg();
    }
}
