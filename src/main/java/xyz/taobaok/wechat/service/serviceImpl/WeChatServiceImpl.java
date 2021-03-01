package xyz.taobaok.wechat.service.serviceImpl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import jd.union.open.goods.promotiongoodsinfo.query.response.PromotionGoodsResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.taobaok.wechat.bean.BaseMessage;
import xyz.taobaok.wechat.bean.Item;
import xyz.taobaok.wechat.bean.NewsMessages;
import xyz.taobaok.wechat.bean.TextMessage;
import xyz.taobaok.wechat.service.WeChatService;
import xyz.taobaok.wechat.toolutil.UrlUtil;
import xyz.taobaok.wechat.toolutil.WechatMessageUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
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


    @Autowired
    WeChatParseEvent weChatParseEvent;
    @Autowired
    DtkApiService dtkApiService;
    @Autowired
    JdApiService jdApiService;


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
        String content = ISEMY;
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
                                try {
                                    String item = dtkApiService.SenDaTaoKeApiGoods(itemId);
                                    if (item.contains("成功")){
                                        JSONObject jsonObject = JSON.parseObject(item);
                                        String data = jsonObject.getString("data");
                                        JSONObject jsonObject1 = JSON.parseObject(data);
                                        String title = jsonObject1.getString("title");
                                        String couponJSon = dtkApiService.senDaTaoKeApiLink(itemId);
                                        if (couponJSon.contains("成功")){
                                            content = dtkApiService.tbItemCouponArrange(title,couponJSon);
                                            break;
                                        }
                                    }
                                } catch (UnsupportedEncodingException e) {
                                    log.error("dtk API is error: Failed to get product information, itemId:{}",itemId);
                                }
                            }
                            break;
                        case "jd":
                            String jdGoodsId = UrlUtil.getUrlItemid(parse.get("url"));
                            if (!jdGoodsId.isEmpty()){
                                PromotionGoodsResp jdItem = null;
                                try {
                                    jdItem = jdApiService.SenJdApiGoods(Arrays.asList(jdGoodsId));
                                } catch (Exception e) {
                                    log.error("union API is error: Failed to get product information, itemId:{}",jdGoodsId);
                                    break;
                                }
                                if (jdItem != null && jdItem.getMaterialUrl() !=null){
                                    msg = new NewsMessages(requestMap,jdItem.getGoodsName(),"描述这里需要修改",jdItem.getImgUrl(),jdItem.getMaterialUrl());
                                }
                            }
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
        if(msg == null){
            msg = new TextMessage(requestMap, content);
        }
        return WechatMessageUtil.beanToXml(msg);
    }
}
