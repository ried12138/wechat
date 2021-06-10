package xyz.taobaok.wechat.bean.dataoke;

import lombok.Data;

import java.io.Serializable;

/**
 * 微信展示订单信息
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2021/6/10   6:16 下午
 * @Version 1.0
 */
@Data
public class WechatOrderDetails implements Serializable {
    /**
     * 商品标题
     */
    private String itemTitle;
    /**
     * 订单编号
     */
    private String tradeParentId;
}
