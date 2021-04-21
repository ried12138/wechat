package xyz.taobaok.wechat.toolutil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
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
     * 时间加减分钟
     * @param startDate 要处理的时间，Null则为当前时间
     * @param minutes 加减的分钟
     * @return Date
     */
    public static String dateAddMinutes(Date startDate, int minutes) {
        if (startDate == null) {
            startDate = new Date();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(startDate);
        c.set(Calendar.MINUTE, c.get(Calendar.MINUTE) + minutes);
        Date time = c.getTime();
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time);
    }


    /**
     * 时间对比
     * @param datea
     * @param dateb
     * @return
     */
    public static boolean dateCompareNow(Date datea,Date dateb) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date dateFirst = null;
        try {
            dateFirst = dateFormat.parse(dateFormat.format(datea));
            Date dateLast = dateFormat.parse(dateFormat.format(dateb));
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
}
