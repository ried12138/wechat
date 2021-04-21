package xyz.taobaok.wechat.bean.dataoke;

import lombok.Data;

/**
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2021/4/16   5:36 下午
 * @Version 1.0
 */
@Data
public class TbOrderDetailsDate {
    private String tb_paid_time;
    private String pay_price;
    private String trade_parent_id;
    private String pub_share_fee;
    private String tk_earning_time;
    private String pub_share_rate;
    private Integer refund_tag;
    private String tk_total_rate;
    private String item_title;
    private Integer tk_status;
    private String item_id;
    private String item_link;
    private String total_commission_fee;
    private Long special_id;
    private Long relation_id;

    private Integer status;
    private String create_time;
    private String update_time;
}
