package xin.pwdkeeper.wechat.toolutil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 时间戳工具
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2021/4/16   4:45 下午
 * @Version 1.0
 */
public class DateTimeUtil {

    // 根据指定格式显示日期和时间
    /** yyyy-MM-dd */
    private static final DateTimeFormatter yyyyMMdd_EN = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    /** yyyy-MM-dd HH */
    private static final DateTimeFormatter yyyyMMddHH_EN = DateTimeFormatter.ofPattern("yyyy-MM-dd HH");
    /** yyyy-MM-dd HH:mm */
    private static final DateTimeFormatter yyyyMMddHHmm_EN = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    /** yyyy-MM-dd HH:mm:ss */
    private static final DateTimeFormatter yyyyMMddHHmmss_EN = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    /**
     * string转date
     * @param time
     * @return
     * @throws ParseException
     */
    public static Date getDefineyyyyMMddHHmmss(String time) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//注意月份是MM
        return simpleDateFormat.parse(time);
    }

    public static Date getDefineyyyyMMddHH(String time) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");//注意月份是MM
        return simpleDateFormat.parse(time);
    }


    /**
     * date转String
     * @param date
     * @return
     */
    public static String getDate(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(date);
    }
    /**
     * 获取当前日期
     * @return
     */
    public static String getNowDate_EN() {
        return String.valueOf(LocalDate.now());
    }

    /**
     * 获取当前日期
     * @return
     */
    public static String getNowTime_EN() {
        return LocalDateTime.now().format(yyyyMMddHHmmss_EN);
    }

    /**
     * 时间加分钟
     * @param minutes 加的分钟
     * @return Date
     */
    public static String datereducedMinutes(Date date ,int minutes) {
        long time = minutes * 60 * 1000;
        Date appoint = null;
        if (date == null) {
            appoint = new Date();
        }else{
            appoint = date;
        }
        appoint = new Date(appoint.getTime() + time);
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(appoint);
    }

    /**
     * 时间减分钟
     * @param minutes 减的分钟
     * @return Date
     */
    public static String dateAddMinutes(int minutes) {
        long time = minutes * 60 * 1000;
         Date date = new Date();
        date = new Date(date.getTime() - time);
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    /**
     * 时间对比 之对比年月日
     * @param datea 对比时间
     * @param nowDate 当前时间
     * @return
     */
    public static boolean dateCompareNow(Date datea,Date nowDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date dateFirst = null;
        try {
            dateFirst = dateFormat.parse(dateFormat.format(datea));
            Date dateLast = dateFormat.parse(dateFormat.format(nowDate));
            return dateFirst.after(dateLast);
//            if (dateFirst.after(dateLast)) {
//                return 1;
//            } else if (dateFirst.before(dateLast)) {
//                return -1;
//            }
        } catch (ParseException e) {
            return false;
        }
    }
    /**
     * 比较给定时间与当前时间，判断两者之间是否在<gap>分钟以内
     * @param timestamp 要比较的时间戳（毫秒）
     * @param gap 差距，相差时间 (分钟)
     * @return 如果两者之间在<gap>分钟以内，返回true；否则返回false
     */
    public static boolean isWithinOneMinute(Long timestamp,int gap) {
        if (timestamp == null) {
            return false;
        }
        long now = System.currentTimeMillis();
        long diffInMillis = Math.abs(now - timestamp);
        long diffInMinutes = diffInMillis / (60 * 1000);
        return diffInMinutes <= 1;
    }
}
