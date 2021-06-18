package xyz.taobaok.wechat.service.serviceImpl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.taobaok.wechat.bean.OrderDetailsInfo;
import xyz.taobaok.wechat.bean.UserWallet;
import xyz.taobaok.wechat.bean.WechatUserInfo;
import xyz.taobaok.wechat.bean.dataoke.OrderConstant;
import xyz.taobaok.wechat.bean.dataoke.TbOrderDetails;
import xyz.taobaok.wechat.service.TbOrderDetailsService;
import xyz.taobaok.wechat.service.UserInfoService;
import xyz.taobaok.wechat.service.UserWalletService;
import xyz.taobaok.wechat.toolutil.TbOrderDetailsTask;
import xyz.taobaok.wechat.toolutil.WechatMessageUtil;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信消息处理server
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2021/2/25   11:46 上午
 * @Version 1.0
 */
@Slf4j
@Service
public class WeChatParseMessage {


    private static final String USER_BIND_STATUS_ERROR = "订单绑定微信失败！请稍后重试，或联系管理人员";
    private static final String USER_BIND_STATUS_REPEAT = "订单已经被绑定成功，无需重复绑定！";
    private static final String USER_BIND_STATUS_SUCCESS = "微信绑定成功！返利信息请在确认收货后到账查询";
    private static final String USER_ORDER_DETAILS_ERROR = "\uD83D\uDE47你的微信号目前还没有绑定订单信息，无法查询返利订单!!";

    private static final String USER_WALLET_ERROR ="\uD83D\uDE45抱歉钱包信息查询失败！请购买成功后将订单发送给我来绑定钱包";
    private static final String USER_WITHD_DETAILS_ERROR = "\uD83D\uDE47自动提现功能暂时不可直接使用，请联系客服进行提现!!";
    @Autowired
    TbOrderDetailsTask tbOrderDetailsTask;
    @Autowired
    DtkApiService dtkApiService;
    @Autowired
    TbOrderDetailsService tbOrderDetailsService;
    @Autowired
    UserInfoService userInfoService;
    @Autowired
    UserWalletService userWalletService;

    /**
     * 辅助指令
     * @param parse
     * @return
     */
    public String getInstruct(Map<String, String> parse) {
        String content = null;
        String fromUserName = parse.get("FromUserName");
        switch (parse.get("instruct")){
            case "客服":
                content = WechatMessageUtil.customerServiceInfo();
                break;
            case "余额":
                UserWallet wallet = userWalletService.queryUserWalletInfo(fromUserName);
                if (wallet != null){
                    content = WechatMessageUtil.userWalletInfo(wallet);
                }else{
                    content = USER_WALLET_ERROR;
                }
                break;
            case "查询":
                WechatUserInfo userInfo = userInfoService.queryUserInfo(fromUserName);
                if (userInfo != null && userInfo.getSpecialId() != null){
                    OrderDetailsInfo odl = tbOrderDetailsService.queryUserOrderDetailsInfo(userInfo.getSpecialId());
                    content = WechatMessageUtil.orderDetailsInfo(odl);
                }else{
                    content = USER_ORDER_DETAILS_ERROR;
                }
                break;
            case "提现":
                content = USER_WITHD_DETAILS_ERROR;
                break;
            default:
                content = WechatMessageUtil.menuText();
                break;
        }
        return content;
    }

    /**
     * 淘宝商品查询转链
     * @param parse
     * @return
     */
    public String getTaobaoConvert(Map<String, String> parse){
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
     * 淘口令解析
     * @param parse
     * @return
     */
    public String getTklConvert(Map<String, String> parse){
        String tpwd = parse.get("tklpwd");
        String fromUserName = parse.get("FromUserName");
        String itemInfo = null;
        String json = dtkApiService.parseTkl(tpwd);
        if (json.contains("成功")){
            String itemid = JSONObject.parseObject(json).getJSONObject("data").getString("goodsId");
            Map<String, String> map = new HashMap<>();
            map.put("id",itemid);
            map.put("FromUserName",fromUserName);
            itemInfo = getTaobaoConvert(map);
        }else{
            String tklConvert = dtkApiService.getTklConvert(tpwd,fromUserName);
            if (tklConvert.contains("成功")){
                itemInfo = dtkApiService.tbItemCouponArrange(null, tklConvert);
            }
            log.info("tklAPI return tklInfo:\n{},\ntpwd:{}",itemInfo,tpwd);
        }
        return itemInfo;
    }

    /**
     * 淘宝订单与微信绑定
     * @param parse
     * @return
     */
    public String getOrderNumberBind(Map<String, String> parse) {
        String tradeParentId = parse.get("orderNumber");
        String status = "";
        //查询订单信息
        log.info("进入订单绑定！！！！");
        for(int i = 0;i< 3;i++){
            TbOrderDetails tbOrderDetails = null;
            try {
                tbOrderDetails = tbOrderDetailsService.selectByPrimaryKey(tradeParentId);
            } catch (Exception e) {
                log.error("查询订单sql失败！！！请检查mysql链接情况");
                e.printStackTrace();
            }
            log.info("订单信息：{}",JSONObject.toJSONString(tbOrderDetails));
            if(tbOrderDetails != null){
                //绑定用户信息
                int label = 0;
                try {
                    label = userInfoService.userInfoBind(parse.get("FromUserName"),tbOrderDetails.getSpecialId(),String.valueOf(System.currentTimeMillis()));
                } catch (Exception e) {
                    log.error("保存用户信息异常");
                    status = USER_BIND_STATUS_ERROR;
                    e.printStackTrace();
                    i = 3;
                    continue;
                }
                if (label == 1){
                    status = USER_BIND_STATUS_SUCCESS;
                }else{
                    status = USER_BIND_STATUS_REPEAT;
                }
                log.info("用户绑定状态：{},微信号：{}",label,parse.get("FromUserName"));
                i = 3;
            }else{
                //拉取最新订单信息 付款时间拉取
                tbOrderDetailsTask.getTbOrderDetails(OrderConstant.PAYMENT_TIME_QUERY, OrderConstant.ORDER_SCENARIO_MEMBER,
                        OrderConstant.ORDER_STATUS_PAYMENT, false);
                status = "抱歉没有查询到订单信息，请重试！或联系管理员";
            }
        }
        return status;
    }
}
