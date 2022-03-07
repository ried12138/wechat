package xyz.taobaok.wechat.bean.dataoke;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 大淘客返回淘宝商品详情
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2022/2/14   12:13
 * @Version 1.0
 */
@ApiModel("淘宝商品详情")
@Data
public class TaobaoItem {
    @ApiModelProperty("商品id,若查询结果id=-1，则说明该商品非大淘客平台商品")
    private Long id;	    				//商品id,若查询结果id=-1，则说明该商品非大淘客平台商品，数据为淘宝直接返回的数据，由于淘宝数据的缓存时间相对较长，会出现商品信息和详情信息不一致的情况	Number	18926311
    @ApiModelProperty("淘宝商品id")
    private String goodsId;					//淘宝商品id															String	“589284195570”
    @ApiModelProperty("商品淘宝链接")
    private String itemLink;				//商品淘宝链接														String	“https://detail.tmall.com/item.htm?id=589284195570“
    @ApiModelProperty("淘宝标题")
    private String title;					//淘宝标题															String	“西维里男士睡衣夏季韩版真丝短袖丝绸薄款宽松青年冰丝家居服套装”
    @ApiModelProperty("大淘客短标题")
    private String dtitle;					//大淘客短标题														String	“夏季睡衣男冰丝短袖丝绸家居服套装”
    @ApiModelProperty("推广文案")
    private String desc;					//推广文案															String	“宽松舒适，难以磨损典，雅而优美，倍感丝滑，质感优越，庄严而心仪，简约设计，色调清新脱俗，适合各种场合”
    @ApiModelProperty("商品在大淘客的分类id")
    private Integer cid;					//商品在大淘客的分类id													Number	10
    @ApiModelProperty("商品在大淘客的二级分类id")
    private List<Integer> subcid;			//商品在大淘客的二级分类id，该分类为前端分类，一个商品可能有多个二级分类id	List[Number]	[86369,90723]
    @ApiModelProperty("商品在淘宝的分类id")
    private Integer tbcid;					//商品在淘宝的分类id													Number	50012772
    @ApiModelProperty("商品主图链接")
    private String mainPic;					//商品主图链接														String	“//img.alicdn.com/imgextra/i2/4014489195/O1CN01kYlVPs2HnMKYwLLRm_!!4014489195.jpg”
    @ApiModelProperty("营销主图链接")
    private String marketingMainPic;		//营销主图链接														String	“https://sr.ffquan.com/super_pic/o_1d6fpckjs1ii3h9dkibk9b7h38.jpg“
    @ApiModelProperty("商品原价")
    private BigDecimal originalPrice;			//商品原价															Number	38.5
    @ApiModelProperty("券后价")
    private BigDecimal actualPrice;			//券后价																number	28.5
    @ApiModelProperty("折扣力度")
    private BigDecimal discounts;				//折扣力度															Number	0.74
    @ApiModelProperty("佣金类型，0-通用，1-定向，2-高佣，3-营销计划")
    private Integer commissionType;			//佣金类型，0-通用，1-定向，2-高佣，3-营销计划							number	3
    @ApiModelProperty("佣金比例")
    private Integer commissionRate;			//佣金比例															Number	20
    @ApiModelProperty("优惠券链接")
    private String couponLink;				//优惠券链接															String	“https://uland.taobao.com/quan/detail?sellerId=4“
    @ApiModelProperty("券总量")
    private Integer couponTotalNum;			//券总量																Number	7000
    @ApiModelProperty("领券量")
    private Integer couponReceiveNum;		//领券量																Number	1000
    @ApiModelProperty("优惠券结束时间")
    private String couponEndTime;			//优惠券结束时间														String	“2019-04-08 23:59:59”
    @ApiModelProperty("优惠券开始时间")
    private String couponStartTime;			//优惠券开始时间														String	“2019-04-01 00:00:00”
    @ApiModelProperty("优惠券金额")
    private Integer couponPrice;			//优惠券金额															Number	10
    @ApiModelProperty("优惠券使用条件")
    private String couponConditions;		//优惠券使用条件														String	“38”
    @ApiModelProperty("30天销量")
    private Integer monthSales;				//30天销量															Number	1030
    @ApiModelProperty("2小时销量")
    private Integer twoHoursSales;			//2小时销量															Number	30
    @ApiModelProperty("当天销量")
    private Integer dailySales;				//当天销量															Number	49
    @ApiModelProperty("是否是品牌商品")
    private Integer brand;					//是否是品牌商品														Number	4
    @ApiModelProperty("品牌id")
    private Long brandId;				    //品牌id																Number	2301323020
    @ApiModelProperty("品牌名称")
    private String brandName;				//品牌名称															String	“西维里”
    @ApiModelProperty("商品上架时间")
    private String createTime;				//商品上架时间														String	“2019-03-29 10:00:06”
    @ApiModelProperty("是否天猫超市商品，1-天猫超市商品，0-非天猫超市商品")
    private Integer tchaoshi;				//是否天猫超市商品，1-天猫超市商品，0-非天猫超市商品						Number	0
    @ApiModelProperty("活动类型，1-无活动，2-淘抢购，3-聚划算")
    private Integer activityType;			//活动类型，1-无活动，2-淘抢购，3-聚划算									Number	1
    @ApiModelProperty("活动开始时间")
    private String activityStartTime;		//活动开始时间														String	“2019-03-29 10:00:06”
    @ApiModelProperty("活动结束时间")
    private String activityEndTime;			//活动结束时间														String	“2019-04-29 10:00:06”
    @ApiModelProperty("店铺类型，1-天猫，0-淘宝")
    private Integer shopType;				//店铺类型，1-天猫，0-淘宝												Number	1
    @ApiModelProperty("是否金牌卖家，1-金牌卖家，0-非金牌卖家")
    private Integer goldSellers;			//是否金牌卖家，1-金牌卖家，0-非金牌卖家									Number	0
    @ApiModelProperty("是否海淘，1-海淘商品，0-非海淘商品")
    private Integer haitao;					//是否海淘，1-海淘商品，0-非海淘商品										Number	0
    @ApiModelProperty("淘宝卖家id")
    private String sellerId;				//淘宝卖家id															String	“4014489195”
    @ApiModelProperty("店铺名称")
    private String shopName;				//店铺名称															String	“西维里旗舰店”
    @ApiModelProperty("淘宝店铺等级")
    private Integer shopLevel;				//淘宝店铺等级														Number	11
    @ApiModelProperty("描述分")
    private Integer descScore;				//描述分																Number	4.8
    @ApiModelProperty("描述相符")
    private Integer dsrScore;				//描述相符															Number	4.8
    @ApiModelProperty("描述同行比")
    private Integer dsrPercent;				//描述同行比															Number	0.0
    @ApiModelProperty("物流服务")
    private Integer shipScore;				//物流服务															Number	4.8
    @ApiModelProperty("物流同行比")
    private Integer shipPercent;			//物流同行比															Number	10.32
    @ApiModelProperty("服务态度")
    private Integer serviceScore;			//服务态度															Number	4.8
    @ApiModelProperty("服务同行比")
    private Integer servicePercent;			//服务同行比															Number	5.82
    @ApiModelProperty("热推值")
    private Integer hotPush;				//热推值																Number	456
    @ApiModelProperty("放单人名称")
    private String teamName;				//放单人名称															String	“啊甘团队”
    @ApiModelProperty("商品主图（需要做适配）")
    private List<String> detailPics;		//商品主图（需要做适配）												Array	[{“img”: “//img.alicdn.com/imgextra/i2/2099112387/O1CN015iPsj11TVHwGWKgLO!!2099112387.jpg”}, {“img”: “//img.alicdn.com/imgextra/
    @ApiModelProperty("淘宝轮播图")
    private List<String> imgs;				//淘宝轮播图															Array
    @ApiModelProperty("相关商品图")
    private List<String> reimgs;			//相关商品图															Array
    @ApiModelProperty("定金，若无定金，则显示0")
    private Integer quanMLink;				//定金，若无定金，则显示0												Number	10
    @ApiModelProperty("立减，若无立减金额，则显示0")
    private Integer hzQuanOver;				//立减，若无立减金额，则显示0											Number	100
    @ApiModelProperty("0.不包运费险 1.包运费险")
    private Integer yunfeixian;				//0.不包运费险 1.包运费险												Number	1
    @ApiModelProperty("预估淘礼金")
    private Integer estimateAmount;			//预估淘礼金															Number	25.2
    @ApiModelProperty("店铺logo")
    private String shopLogo;				//店铺logo															String	https://img.alicdn.com/imgextra//59/df/TB1lJVxNFXXXXcoXFXXSutbFXXX.jpg
    @ApiModelProperty("商品卖点")
    private String specialText;				//商品卖点															List
}