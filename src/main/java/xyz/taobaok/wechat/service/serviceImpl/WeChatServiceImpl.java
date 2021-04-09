package xyz.taobaok.wechat.service.serviceImpl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pdd.pop.sdk.http.api.pop.response.PddDdkGoodsDetailResponse;
import jd.union.open.goods.promotiongoodsinfo.query.response.PromotionGoodsResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.taobaok.wechat.bean.BaseMessage;
import xyz.taobaok.wechat.bean.Item;
import xyz.taobaok.wechat.bean.NewsMessages;
import xyz.taobaok.wechat.bean.TextMessage;
import xyz.taobaok.wechat.service.WeChatService;
import xyz.taobaok.wechat.toolutil.TpwdUtil;
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
    @Autowired
    PddApiServiceImpl pddApiService;


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
        log.info("被请求了！！！！！！请求信息为：{}",requestMap.toString());
        switch (requestMap.get("MsgType")){
            case WechatMessageUtil.RESP_MESSAGE_TYPE_TEXT:  //文本
                Map<String, String> parse = UrlUtil.parse(requestMap.get("Content"));
                String platform = parse.get("platform");
                if (platform != null){
                    switch (platform){
                        case "tb":
                            if (!getTaobaoConvert(parse).isEmpty()){
                                content = getTaobaoConvert(parse);
                            }
                            break;
                        case "jd":
                            if (getJdConvert(parse, requestMap) !=null){
                                msg = getJdConvert(parse, requestMap);
                            }
                            break;
                        case "pdd":
                            content = "接官方通知，即日起(2020年9月5日)拼多多暂不支持返li查询，恢复时间另行通知";
//                            String goodsId = parse.get("goods_id");
//                            if (!goodsId.isEmpty()){
//                                PddDdkGoodsDetailResponse.GoodsDetailResponseGoodsDetailsItem goodsDetailsItem = null;
//                                try {
//                                    goodsDetailsItem = pddApiService.senPindDuoDuoApiGoods(goodsId);
//                                } catch (Exception e) {
//                                    log.error("pdd API is error: Failed to get product information, itemId:{}",goodsId);
//                                }
//                                String goodsSign = goodsDetailsItem.getGoodsSign();
//                                System.out.println(goodsSign);
//                            }
                            break;
                        default:
                            break;
                    }
                }else{
                    //淘口令
                    String tpwd = TpwdUtil.isTpwd(parse.get("url"));
                    if (tpwd != null){
                        content = getTklConvert(tpwd) ==null? ISEMY:getTklConvert(tpwd);
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
        return WechatMessageUtil.beanToXml(msg);
    }

    /**
     * 淘宝商品查询转链
     * @param parse
     * @return
     */
    private String getTaobaoConvert(Map<String, String> parse){
        String itemId = parse.get("id");
        String content = "";
        if (itemId != null){
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
                    }
                }
            } catch (UnsupportedEncodingException e) {
                log.error("dtk API is error: Failed to get product information, itemId:{}",itemId);
            }
        }
        return content;
    }

    /**
     * 京东商品查询转链
     * @param parse
     * @param requestMap
     * @return
     */
    private BaseMessage getJdConvert(Map<String, String> parse,Map<String, String> requestMap){
        BaseMessage msg = null;
        String jdGoodsId = UrlUtil.getUrlItemid(parse.get("url"));
        if (!jdGoodsId.isEmpty()){
            PromotionGoodsResp jdItem = null;
            try {
                jdItem = jdApiService.SenJdApiGoods(Arrays.asList(jdGoodsId));
                if (jdItem != null && jdItem.getMaterialUrl() !=null){
                    String goodsName = jdItem.getGoodsName().substring(0, 20);
                    msg = new NewsMessages(requestMap,"【"+goodsName+"...】",
                            "找到商品了！点击即可购买"+"\n"+
                                    "价格："+jdItem.getWlUnitPrice()+"\n" +
                                    "类别："+jdItem.getCidName()+"\n"+
                                    "优惠券信息：该商品暂无优惠券！"
                            ,jdItem.getImgUrl(),jdItem.getMaterialUrl());
                }
            } catch (Exception e) {
                log.error("union API is error: Failed to get product information, itemId:{}",jdGoodsId);
            }
        }
        return msg;
    }


    /**
     * 淘口令解析
     * @param tpwd
     * @return
     */
    private String getTklConvert(String tpwd){
        try {
            String tklConvert = dtkApiService.getTklConvert(tpwd);
            if (tklConvert.contains("成功")){
                return dtkApiService.tbItemCouponArrange(null, tklConvert);
            }
        } catch (UnsupportedEncodingException e) {
            log.error("tkl API is error: Failed to get product information, tpwd:{}",tpwd);
        }
        return null;
    }
}
