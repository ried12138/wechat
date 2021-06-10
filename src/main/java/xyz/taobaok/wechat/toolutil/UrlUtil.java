package xyz.taobaok.wechat.toolutil;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * url解析工具
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2021/2/25   6:21 下午
 * @Version 1.0
 */
public class UrlUtil {

    private static Pattern ch_EN = Pattern.compile("[\u4e00-\u9fa5]");
    private static Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
    private static final String regEx = "[^0-9]";

    //获取url参数
    public static Map<String,String> parse(String url) {
        Map<String,String> map = new HashMap<>();
        if (url == null) {
            return map;
        }
        url = url.trim();
        if (url.isEmpty()) {
            return map;
        }
        String[] urlParts = url.split("\\?");
        //没有参数
        if (urlParts.length == 1) {
            int length = urlParts[0].length();
            if (isInteger(urlParts[0])){
                //用户订单
                map.put("platform","order");
                map.put("orderNumber",urlParts[0]);
                return map;
            }else if (TpwdUtil.isTpwd(urlParts[0]) != null){
                //淘口令
                map.put("platform","tklpwd");
                map.put("tklpwd",TpwdUtil.isTpwd(urlParts[0]));
                return map;
            }else if(ch_EN.matcher(urlParts[0]).find() && urlParts[0].length() == 2){
                //指令
                map.put("platform","instruct");
                map.put("instruct",urlParts[0]);
                return map;
            }
        }
//        map.put("url",urlParts[0]);
        //有参数
        if (urlParts[0].contains("taobao.com") ||urlParts[0].contains("tmall.com")){
            map.put("platform","tb");
        }else if(urlParts[0].contains("jd.com") ||urlParts[0].contains("jd.hk")){
            map.put("platform","jd");
            map.put("url",urlParts[0]);
        }else if (urlParts[0].contains("yangkeduo.com")){
            map.put("platform","pdd");
        }
        if(urlParts.length >1){
            String[] params = urlParts[1].split("&");
            for (String param : params) {
                if (!param.equals("")){
                    String[] keyValue = param.split("=");
                    map.put(keyValue[0], keyValue[1]);
                }
            }
        }
        return map;
    }


    /**
     * 获取url中商品id
     * @param url
     * @return
     */
    public static String getUrlItemid(String url){
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(url);
        return m.replaceAll("").trim();
    }

    /**
     * 判断是否为整数
     * @param str 传入的字符串
     * @return 是整数返回true,否则返回false
     */
    public static boolean isInteger(String str) {
        return pattern.matcher(str).matches();
    }
}
