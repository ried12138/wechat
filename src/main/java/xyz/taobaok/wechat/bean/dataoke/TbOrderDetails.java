package xyz.taobaok.wechat.bean.dataoke;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * tb_order_details
 * @author 
 */
@Data
public class TbOrderDetails implements Serializable {
    /**
     * id
     */
    private Integer id;

    /**
     * 订单在淘宝拍下付款的时间
     */
    private Date tbPaidTime;

    /**
     * 买家确认收货的付款金额（不包含运费金额）
     */
    private BigDecimal payPrice;

    /**
     * 买家在淘宝后台显示的订单编号
     */
    private String tradeParentId;

    /**
     * 结算预估收入=结算金额×提成。以买家确认收货的付款金额为基数，预估您可能获得的收入。因买家退款、您违规推广等原因，可能与您最终收入不一致。最终收入以月结后您实际收到的为准
     */
    private BigDecimal pubShareFee;

    /**
     * 订单确认收货后且商家完成佣金支付的时间
     */
    private Date tkEarningTime;

    /**
     * 从结算佣金中分得的收益比率
     */
    private String pubShareRate;

    /**
     * 维权标签，0 含义为非维权 1 含义为维权订单
     */
    private String refundTag;

    /**
     * 提成=收入比率×分成比率。指实际获得收益的比率
     */
    private BigDecimal tkTotalRate;

    /**
     * 商品标题
     */
    private String itemTitle;

    /**
     * 已付款：指订单已付款，但还未确认收货 已收货：指订单已确认收货，但商家佣金未支付 已结算：指订单已确认收货，且商家佣金已支付成功 已失效：指订单关闭/订单佣金小于0.01元，订单关闭主要有：1）买家超时未付款； 2）买家付款前，买家/卖家取消了订单；3）订单付款后发起售中退款成功；3：订单结算，12：订单付款， 13：订单失效，14：订单成功
     */
    private Integer tkStatus;

    /**
     * 商品id
     */
    private String itemId;

    /**
     * 商品链接
     */
    private String itemLink;

    /**
     * 佣金金额=结算金额＊佣金比率
     */
    private String totalCommissionFee;

    /**
     * 会员运营id ;淘宝授权用户标示值
     */
    private String specialId;

    /**
     * 渠道关系id
     */
    private String relationId;

    /**
     * 订单状态，0=未完成(返利未发放)，1=已完成(返利已发放)
     */
    private Integer status;

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