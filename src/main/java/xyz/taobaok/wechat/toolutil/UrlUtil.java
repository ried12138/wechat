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
        map.put("url",urlParts[0]);
        //没有参数
        if (urlParts.length == 1) {
            return map;
        }
        //有参数
        if (urlParts[0].contains("taobao.com") ||urlParts[0].contains("tmall.com")){
            map.put("platform","tb");
        }else if(urlParts[0].contains("jd.com") ||urlParts[0].contains("jd.hk")){
            map.put("platform","jd");
        }else if (urlParts[0].contains("yangkeduo.com")){
            map.put("platform","pdd");
        }
        String[] params = urlParts[1].split("&");
        for (String param : params) {
            String[] keyValue = param.split("=");
            map.put(keyValue[0], keyValue[1]);
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
}
