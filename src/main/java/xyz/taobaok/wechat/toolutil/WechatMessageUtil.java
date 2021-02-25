package xyz.taobaok.wechat.toolutil;

import com.thoughtworks.xstream.XStream;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import xyz.taobaok.wechat.bean.BaseMessage;
import xyz.taobaok.wechat.bean.TextMessage;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2021/2/25   3:26 下午
 * @Version 1.0
 */
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
//        stream.processAnnotations(NewsMessage.class);
//        stream.processAnnotations(VideoMessage.class);
//        stream.processAnnotations(VoiceMessage.class);
        String xml = stream.toXML(msg);
        return xml;
    }



    //关注事件回复信息内容
    public static String menuText() {
        return "感谢关注涿鹿领券分享！\n获取优惠券方式，请直接：\n打开【手机淘宝】————>\n选择要购买的【商品】————>" +
                "\n进入商品【详情页】————>\n长按商品名称【复制宝贝链接】————>\n打开微信————>\n进入【涿鹿领券分享】————>\n粘贴标题查询优惠券\n或登陆网站http://www.taobaok.xyz 里面的商品全部有隐藏优惠券哦！";
    }
}
