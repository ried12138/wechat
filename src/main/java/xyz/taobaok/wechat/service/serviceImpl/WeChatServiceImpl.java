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
import xyz.taobaok.wechat.bean.dataoke.TbOrderConstant;
import xyz.taobaok.wechat.bean.dataoke.TbOrderDetails;
import xyz.taobaok.wechat.mapper.TbOrderDetailsMapper;
import xyz.taobaok.wechat.mapper.WechatUserInfoMapper;
import xyz.taobaok.wechat.service.UserInfoService;
import xyz.taobaok.wechat.service.WeChatService;
import xyz.taobaok.wechat.toolutil.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
    private static final String USER_BIND_STATUS_ERROR = "订单绑定微信失败！请稍后重试，或联系管理人员";
    private static final String USER_BIND_STATUS_SUCCESS = "微信绑定成功！返利信息请在确认收货后到账查询";
    private static final String TEXTERROR = "请输入正确的商品链接或者淘口令！\n目前支持淘宝、天猫、京东商品优惠信息";


    @Autowired
    WeChatParseEvent weChatParseEvent;
    @Autowired
    DtkApiService dtkApiService;
    @Autowired
    JdApiService jdApiService;
    @Autowired
    PddApiServiceImpl pddApiService;
    @Autowired
    TbOrderDetailsMapper tbOrderDetailsMapper;
    @Autowired
    UserInfoService userInfoService;
    @Autowired
    TbOrderDetailsTask tbOrderDetailsTask;


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
        //获取微信用户信息
        String fromUserName = requestMap.get("FromUserName");
        log.info("被请求了！！！！！！请求信息为：{}",requestMap.toString());
        switch (requestMap.get("MsgType")){
            case WechatMessageUtil.RESP_MESSAGE_TYPE_TEXT:  //文本
                Map<String, String> parse = UrlUtil.parse(requestMap.get("Content"));
                String platform = parse.get("platform");
                parse.put("FromUserName",fromUserName);
                if (platform != null){
                    switch (platform){
                        case "tb":
                            content = getTaobaoConvert(parse);
                            content = content.isEmpty() ? ISEMY : content;
                            break;
                        case "jd":
                            msg = getJdConvert(parse, requestMap);
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
                            //订单
                        case "order":
                            content = getOrderNumberBind(parse);
                            break;
                        default:
                            break;
                    }
                }else{
                    //淘口令
                    String tpwd = TpwdUtil.isTpwd(parse.get("url"));
                    if (tpwd != null){
                        content = getTklConvert(tpwd,fromUserName);
                        content = content == null? ISEMY:content;
                    }else{
                        msg = new TextMessage(requestMap, TEXTERROR);
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
     * 淘宝订单与微信绑定
     * @param parse
     * @return
     */
    private String getOrderNumberBind(Map<String, String> parse) {
        String tradeParentId = parse.get("orderNumber");
        String status = "";
        //查询订单信息
        log.info("进入订单绑定！！！！");
        for(int i = 0;i< 3;i++){
            TbOrderDetails tbOrderDetails = null;
            try {
                tbOrderDetails = tbOrderDetailsMapper.selectByPrimaryKey(tradeParentId);
            } catch (Exception e) {
                log.error("查询订单sql失败！！！请检查mysql链接情况");
                e.printStackTrace();
            }
            log.info("订单信息：{}",JSONObject.toJSONString(tbOrderDetails));
            if(tbOrderDetails != null){
                //绑定用户信息
                int label = 0;
                try {
                    label = userInfoService.userInfoBind(parse.get("FromUserName"),tbOrderDetails.getSpecialId(),parse.get("FromUserName"));
                } catch (Exception e) {
                    log.error("保存用户信息异常");
                    e.printStackTrace();
                }
                if (label == 1){
                    status = USER_BIND_STATUS_SUCCESS;
                }else{
                    status = USER_BIND_STATUS_ERROR;
                }
                log.info("用户绑定状态：{},微信号：{}",label,parse.get("FromUserName"));
                i = 3;
            }else{
                //拉取最新订单信息 付款时间拉取
                tbOrderDetailsTask.getTbOrderDetails(TbOrderConstant.PAYMENT_TIME_QUERY, TbOrderConstant.ORDER_SCENARIO_MEMBER,
                        TbOrderConstant.ORDER_STATUS_PAYMENT, false);
            }
        }
        return status;
    }


    /**
     * 淘宝商品查询转链
     * @param parse
     * @return
     */
    private String getTaobaoConvert(Map<String, String> parse){
        String itemId = parse.get("id");
        String fromUserName = parse.get("FromUserName");
        String content = "";
        if (itemId != null){
            try {
                String item = dtkApiService.SenDaTaoKeApiGoods(itemId);
                if (item.contains("成功")){
                    JSONObject jsonObject = JSON.parseObject(item);
                    String data = jsonObject.getString("data");
                    JSONObject jsonObject1 = JSON.parseObject(data);
                    String title = jsonObject1.getString("title");
                    String couponJSon = dtkApiService.senDaTaoKeApiLink(itemId,fromUserName);
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
     * @param fromUserName
     * @return
     */
    private String getTklConvert(String tpwd, String fromUserName){
        String itemInfo = null;
        String json = dtkApiService.parseTkl(tpwd);
        if (json.contains("成功")){
            String itemid = JSONObject.parseObject(json).getJSONObject("data").getString("goodsId");
            Map<String, String> parse = new HashMap<>();
            parse.put("id",itemid);
            parse.put("FromUserName",fromUserName);
            itemInfo = getTaobaoConvert(parse);
        }else{
            String tklConvert = dtkApiService.getTklConvert(tpwd,fromUserName);
            if (tklConvert.contains("成功")){
                itemInfo = dtkApiService.tbItemCouponArrange(null, tklConvert);
            }
            log.info("tklAPI return tklInfo:\n{},\ntpwd:{}",itemInfo,tpwd);
        }
        return itemInfo;
    }
}
