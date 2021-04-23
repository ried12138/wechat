package xyz.taobaok.wechat.bean;

import lombok.Data;

import java.util.Date;

/**
 * 订单最小时间和最大时间
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2021/4/23   6:30 下午
 * @Version 1.0
 */
@Data
public class MaxMinCreateTime {

    private Date minTime;
    private Date MaxTime;
}
