package xyz.taobaok.wechat.service;

import xyz.taobaok.wechat.bean.MaxMinCreateTime;
import xyz.taobaok.wechat.bean.OrderDetailsInfo;
import xyz.taobaok.wechat.bean.dataoke.TbOrderDetails;

/**
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2021/4/23   4:48 下午
 * @Version 1.0
 */
public interface TbOrderDetailsService {

    TbOrderDetails selectByPrimaryKey(String tradeParentId);

    OrderDetailsInfo queryUserOrderDetailsInfo(String specialId);

    int updateByPrimaryKeySelective(TbOrderDetails tbODs);

    int insertSelective(TbOrderDetails tbODs);

    MaxMinCreateTime allTkStatusPayment();
}
