package xyz.taobaok.wechat.toolutil;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

/**
 * @Title: SignMD5Util
 * @Description: 签名相关工具类
 * @Version V1.0
 */
public class SignMD5Util {

    /**
     * 获取签名的util
     *
     * @param map       请求参数
     * @param secretKey 密钥
     * @return
     */
    public static String getSignStr(TreeMap<String, Object> map, String secretKey) {
        if (map.size() == 0) {
            return "";
        }
        StringBuffer sb = new StringBuffer("");

        Set<String> keySet = map.keySet();
        Iterator<String> iter = keySet.iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            sb.append("&" + key + "=" + map.get(key));
        }
        sb.deleteCharAt(0);
        return sign(sb.toString(), secretKey);
    }

    public static String sign(String content, String key) {
        String signStr = "";
        signStr = content + "&key=" + key;
        //MD5加密后，字符串所有字符转换为大写
        return MD5(signStr).toUpperCase();
    }

    /**
     * MD5加密算法
     *
     * @param s
     * @return
     * @see [类、类#方法、类#成员]
     */
    public final static String MD5(String s) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            byte[] btInput = s.getBytes();
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str).toLowerCase();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getStringDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateString = formatter.format(currentTime);
        return dateString;
    }
}

