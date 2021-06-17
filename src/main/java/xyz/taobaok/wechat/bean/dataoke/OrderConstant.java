package xyz.taobaok.wechat.bean.dataoke;

/**
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2021/4/16   5:18 下午
 * @Version 1.0
 */
public class OrderConstant {
    public static final Integer CREATE_TIME_QUERY = 1;                 //按照订单淘客创建时间查询
    public static final Integer PAYMENT_TIME_QUERY = 2;                //按照订单淘客付款时间查询
    public static final Integer SETTLEMENT_TIME_QUERY = 3;             //按照订单淘客结算时间查询
    public static final Integer UPDATE_TIME_QUERY = 4;             //按照订单更新时间查询

    public static final Integer ORDER_STATUS_PAYMENT = 12;             //淘客订单状态，付款
    public static final Integer ORDER_STATUS_CLOSE = 13;               //淘客订单状态，关闭
    public static final Integer ORDER_STATUS_RECEIV = 14;              //淘客订单状态，确认收货
    public static final Integer ORDER_STATUS_SUCCESS = 3;              //淘客订单状态，结算成功

    public static final Integer ORDER_SCENARIO_ROUTINE = 1;            //场景订单场景类型，常规订单
    public static final Integer ORDER_SCENARIO_CHANNEL = 2;            //场景订单场景类型，渠道订单
    public static final Integer ORDER_SCENARIO_MEMBER = 3;             //场景订单场景类型，会员运营订单


    public static final Integer ORDER_PLACE_TIME = 1;             //京东订单状态，下单时间
    public static final Integer ORDER_COMPLETE_TIME = 2;          //京东订单状态，完成时间
    public static final Integer ORDER_UPDATE_TIME = 3;            //京东订单状态，更新时间


    public static final Integer JD_ORDER_STATUS_RECEIV = 17;              //京东订单 确认收货
    public static final Integer JD_ORDER_STATUS_SUCCESS = 16;              //京东订单 付款



    public static final Integer REBATE_STATUS = 0;          //返利未发放的
    public static final Integer REBATE_STATUS_N = 2;          //返利未发放的
}
