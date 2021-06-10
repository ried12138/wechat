package xyz.taobaok.wechat.toolutil;

import com.thoughtworks.xstream.XStream;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import xyz.taobaok.wechat.bean.*;
import xyz.taobaok.wechat.bean.dataoke.TbOrderDetails;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Email liuweiran12138@outlook.com
 * @Date 2021/2/25   3:26 下午
 * @Version 1.0
 */
@Slf4j
public class WechatMessageUtil {
    //微信校验token
    public static final String TOKEN = "0ADCEAF6CCE07DF3";
    /**
     * 消息常量
     */
    public static final String RESP_MESSAGE_TYPE_TEXT = "text";         //文本
    public static final String RESP_MESSAGE_TYPE_IMG = "image";         //图片
    public static final String RESP_MESSAGE_TYPE_LINK = "link";         //微信支持url
    /**
     * 事件常量
     */
    public static final String REQ_MESSAGE_TYPE_EVENT = "event";        //事件
    public static final String EVENT_TYPE_UNSUBSCRIBE = "unsubscribe";  //取消关注
    public static final String EVENT_TYPE_SUBSCRIBE = "subscribe";      //关注
    public static final String EVENT_TYPE_CLICK = "CLICK";              //点击

    /**
     * 微信消息流处理
     * @param request
     * @return
     */
    public static Map<String, String> parseXml(HttpServletRequest request) {
        // 将解析结果存储在HashMap中
        Map<String, String> map = new HashMap<String, String>();
        //从request中取得输入流
        ServletInputStream inputStream = null;
        try {
            inputStream = request.getInputStream();
            //读取流
            SAXReader saxReader = new SAXReader();
            Document document = null;
            try {
                document = saxReader.read(inputStream);
            } catch (DocumentException e) {
                log.error("获取requestinfo内容流异常！！！");
                inputStream.close();
                e.printStackTrace();
            }
            //得到xml根元素
            Element root = document.getRootElement();
            //得到根元素得所有子节点
            List<Element> elements = root.elements();
            //遍历子节点
            for (Element element : elements) {
                map.put(element.getName(), element.getText());
            }
            //释放关流
            inputStream.close();
        } catch (IOException e) {
            log.error("获取requestinfo内容流异常！！！");
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 微信校验token加密
     * @param byteArray
     * @return
     */
    public static String byteTStr(byte[] byteArray){
        String strDigest = "";
        for (int i = 0; i < byteArray.length; i++) {
            strDigest += byteToHexStr(byteArray[i]);
        }
        return strDigest;
    }
    private static String byteToHexStr(byte mByte){
        char[] Digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] tempArr = new char[2];
        tempArr[0] = Digit[(mByte >>> 4) & 0X0F];
        tempArr[1] = Digit[mByte & 0X0F];
        String s = new String(tempArr);
        return s;
    }



    public static String beanToXml(BaseMessage msg) {
        XStream stream = new XStream();
        //设置需要处理XStreamAlias("xml")注解的类
        stream.processAnnotations(TextMessage.class);
//        stream.processAnnotations(ImageMessage.class);
//        stream.processAnnotations(MusicMessage.class);
        stream.processAnnotations(NewsMessages.class);
//        stream.processAnnotations(VideoMessage.class);
//        stream.processAnnotations(VoiceMessage.class);
        String xml = stream.toXML(msg);
        return xml;
    }



    //关注事件回复信息内容
    public static String menuText() {
        return "感谢关注领券分享折扣网！-公众号目前支持淘宝、天猫、京东等电商平台，通过公众号购物可享受返利！AI智能全网检索商品优惠信息，下单后即可获取返利\n一一一一一一一一一一一一\n[[Packet]【返利步骤】如下：\n" +
                "1、复制【淘宝】、【天猫】、【京东】商品链接发送给我\n" +
                "2、全网智能搜索商品优惠和返利信息！！\n" +
                "3、付款成功后，复制订单号并发送给我绑定返利\n" +
                "\uD83D\uDC8C 其他【帮助指令】：\n" +
                "\n" +
                "发送“余额”可以查看余额等个人信息\n" +
                "发送“查询”可以查询最近订单记录\n" +
                "发送“提现”可以提取当前余额\n" +
                "发送“客服”大西湾往东600米\uD83D\uDC4F";
    }

    //客服信息内容
    public static String customerServiceInfo(){
        return "\uD83D\uDCAC【客服信息】：\n" +
                "  微信：autonomy_developer";
    }
    //用户下单详情信息内容
    public static String orderDetailsInfo(OrderDetailsInfo tbod){
        StringBuffer str = new StringBuffer("\uD83D\uDC8C【最近订单详情】\n" +
                "总共：" + tbod.getNum() + ",笔\n" +
                "结算订单：" + tbod.getSettlementOrder() + ",笔\n" +
                "付款订单：" + tbod.getPaymentOrder() + ",笔\n" +
                "只能显示近期10条记录\n\n"+
                "————自己订单————\n");
        List<TbOrderDetails> details = tbod.getDetails();
        for (TbOrderDetails detail : details) {
            str.append("订单编号："+detail.getTradeParentId()+"\n");
        }
        return str.toString();
    }

    /**
     * 钱包信息内容
     * @param wallet
     * @return
     */
    public static String userWalletInfo(UserWallet wallet){
        BigDecimal cumulationIncome = wallet.getCumulationIncome();
        cumulationIncome = cumulationIncome == null ? BigDecimal.valueOf(0.00) : cumulationIncome;
        BigDecimal pubShareFee = wallet.getPubShareFee();
        pubShareFee = pubShareFee == null ? BigDecimal.valueOf(0.00) : pubShareFee;
        return "\uD83C\uDF89【钱包信息】\n" +
                "\n" +
                "已提现金额:"+cumulationIncome+"元\n" +
                "可提现金额:"+wallet.getBalance()+"元\n" +
                "提现中:"+wallet.getExtracting()+"元\n" +
                "*******************\n" +
                "未收货金额:"+pubShareFee+"元\n";

    }
}
