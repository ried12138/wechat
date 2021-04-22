package xyz.taobaok.wechat.toolutil;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 淘口令工具类
 */
@Component
@Slf4j
public class TpwdUtil {

    /**
     * 淘口令格式
     */
    private static final Pattern TPWD_PATTERN = Pattern.compile("[\\p{Sc}\\(\\)\\《\\u5fae\\uff0c\\u4fe1\\u554a\\u54c8\\uff01\\u4fe1\\u563b](\\w{11,20})[\\p{Sc}\\(\\)\\《\\u5fae\\uff0c\\u4fe1\\u554a\\u54c8\\uff01\\u4fe1\\u563b]");
    //不能使用的淘口令正则
    private Pattern TEST = Pattern.compile("[؋\u200E฿₿¢₡₵$₫֏€ƒ₲₾₴₭₺₼₥₦₱£﷼\u200E៛ރ₽₨௹₹৲৳૱₪₸₮₩¥₳₠₢₯₣₤₶ℳ₧₰₷￥$€(《₤₳¢₲£₴₪₰₵\uD83D\uDCB0\uD83C\uDF81\uD83D\uDD11\uD83D\uDCF2\uD83D\uDD10\uD83D\uDDDD\uD83D\uDE3A\uD83D\uDE38\uD83D\uDCB2✔\uD83C\uDFB5(]([a-zA-Z0-9]{11})[)؋\u200E฿₿¢₡₵$₫֏€ƒ₲₾₴₭₺₼₥₦₱£﷼\u200E៛ރ₽₨௹₹৲৳૱₪₸₮₩¥₳₠₢₯₣₤₶ℳ₧₰₷￥$€(《₤₳¢₲£₴₪₰₵\uD83D\uDCB0\uD83C\uDF81\uD83D\uDD11\uD83D\uDCF2\uD83D\uDD10\uD83D\uDDDD\uD83D\uDE3A\uD83D\uDE38\uD83D\uDCB2✔\uD83C\uDFB5]");
    /**
     * emoji表情正则
     */
    private static final Pattern EmojiPattern = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]");

    /**
     * 判断是否为淘口令
     * 是，返回
     * 否，null
     * @param str
     * @return
     */
    public static String isTpwd(String str) {
        if (str.isEmpty()) {
            return null;
        }
        //判断字符串超过13长度
        StringBuffer sb = new StringBuffer();
        if (str.length() > 13){
            if (isHasEmoji(str)){
                Matcher matcher = EmojiPattern.matcher(str);
                while (matcher.find()){
                    //将emoji表情替换测$符号
                    matcher.appendReplacement(sb, Matcher.quoteReplacement("$"));
                }
                matcher.appendTail(sb);
                Matcher matcherIs = TPWD_PATTERN.matcher(sb);
                if (matcherIs.find()){
                    return matcherIs.group();
                }
            }
            Matcher matcher = TPWD_PATTERN.matcher(str);
            if (matcher.find()){
                return matcher.group();
            }
        }
       return null;
    }

    /**
     * 获取淘口令中的关键字，匹配不到返回空字符串
     *
     * @param str
     * @return
     */
    public String getTpwdKey(String str) {
        if (str.isEmpty()) {
            return "";
        }
        Matcher matcher = TPWD_PATTERN.matcher(str);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }

    /**
     * 获取淘口令
     * @param str
     * @return
     */
    public String getTpwdKeyV2(String str){
        Matcher matcher = TPWD_PATTERN.matcher(str);
        StringBuffer stringBuffer = new StringBuffer();
        while (matcher.find()){
            stringBuffer.append(matcher.group());
        }
        return stringBuffer.toString();
    }

    /**
     * 判断字符串是否含有Emoji表情
     */
    private static boolean isHasEmoji(String reviewerName) {
        Matcher matcher = EmojiPattern.matcher(reviewerName);
        return matcher.find();
    }


    /**
     * 淘口令截取替换前后符号
     * @param tpwd
     * @return
     */
    public static String headAndTailSubstitution(String tpwd){
        String substring = tpwd.substring(1, tpwd.length() - 1);
        return new StringBuffer("¥" + substring + "¥").toString();
    }
}
