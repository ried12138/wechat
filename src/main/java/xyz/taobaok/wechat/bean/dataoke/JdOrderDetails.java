package xyz.taobaok.wechat.bean.dataoke;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * jd_order_details
 * @author 
 */
@Data
public class JdOrderDetails implements Serializable {
    /**
     * 标记唯一订单行：订单+sku维度的唯一标识
     */
    private String id;

    /**
     * 订单号
     */
    private Long orderid;

    /**
     * 父单的订单号：如一个订单拆成多个子订单时，原订单号会作为父单号，拆分的订单号为子单号存储在orderid中。若未发生拆单，该字段为0
     */
    private Long parentid;

    /**
     * 下单时间
     */
    private Date ordertime;

    /**
     * 完成时间
     */
    private Date finishtime;

    /**
     * 更新时间
     */
    private Date modifytime;

    /**
     * 下单用户是否为PLUS会员 0：否，1：是
     */
    private Integer plus;

    /**
     * 商品id
     */
    private Long skuid;

    /**
     * 商品名称
     */
    private String skuname;

    /**
     * 实际计算佣金的金额。订单完成后，会将误扣除的运费券金额更正。如订单完成后发生退款，此金额会更新。
     */
    private String actualcosprice;

    /**
     * sku维度的有效码（-1：未知,2.无效-拆单,3.无效-取消,4.无效-京东帮帮主订单,5.无效-账号异常,6.无效-赠品类目不返佣,7.无效-校园订单,8.无效-企业订单,9.无效-团购订单,11.无效-乡村推广员下单,13.无效-违规订单,14.无效-来源与备案网址不符,15.待付款,16.已付款,17.已完成（购买用户确认收货）,20.无效-此复购订单对应的首购订单无效,21.无效-云店订单
     */
    private Integer validcode;

    /**
     * 对外开放用户标示，关联id
     */
    private String subunionid;

    /**
     * 预估结算时间，订单完成后才会返回，格式：yyyyMMdd，默认：0。表示最新的预估结算日期。当payMonth为当前的未来时间时，表示该订单可结算；当payMonth为当前的过去时间时，表示该订单已结算
     */
    private String paymonth;

    /**
     * 订单入库时间
     */
    private Date createTime;

    /**
     * 订单最后一次修改时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}