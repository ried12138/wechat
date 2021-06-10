package xyz.taobaok.wechat.bean;

import lombok.Data;
import xyz.taobaok.wechat.bean.dataoke.TbOrderDetails;
import xyz.taobaok.wechat.bean.dataoke.WechatOrderDetails;

import java.util.List;

/**
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2021/4/23   4:09 下午
 * @Version 1.0
 */
@Data
public class OrderDetailsInfo {
    private int num;                                 //订单总数
    private int settlementOrder;                     //结算订单
    private int paymentOrder;                        //付款订单
    private List<WechatOrderDetails> details;        //订单详情


}
