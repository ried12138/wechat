package xyz.taobaok.wechat.service.serviceImpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.taobaok.wechat.bean.MaxMinCreateTime;
import xyz.taobaok.wechat.bean.OrderDetailsInfo;
import xyz.taobaok.wechat.bean.dataoke.TbOrderDetails;
import xyz.taobaok.wechat.mapper.TbOrderDetailsMapper;
import xyz.taobaok.wechat.service.TbOrderDetailsService;

import java.util.List;

/**
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2021/4/23   4:48 下午
 * @Version 1.0
 */
@Slf4j
@Service
public class TbOrderDetailsServiceImpl implements TbOrderDetailsService {

    @Autowired
    TbOrderDetailsMapper tbOrderDetailsMapper;


    @Override
    public TbOrderDetails selectByPrimaryKey(String tradeParentId) {
        return tbOrderDetailsMapper.selectByPrimaryKey(tradeParentId);
    }

    /**
     * 通过用户specialId来获取个人订单信息
     * @param specialId
     * @return
     */
    @Override
    public OrderDetailsInfo queryUserOrderDetailsInfo(String specialId) {
        //查询总订单数
        Integer num = tbOrderDetailsMapper.selectByPrimarySpecialId(specialId);
        //查询已付款订单数
        Integer odInt = tbOrderDetailsMapper.selectSpecialIdtkStatus(specialId,12);
        //查询结算订单数
        Integer okInt = tbOrderDetailsMapper.selectSpecialIdtkStatus(specialId,3);
        //获取近期是个订单信息
        List<TbOrderDetails> tbod = tbOrderDetailsMapper.selectByPrimarySpecialIdInfo(specialId,10);
        OrderDetailsInfo odInfo = new OrderDetailsInfo();
        odInfo.setNum(num != null ? num : 0);
        odInfo.setPaymentOrder(odInt != null ? odInt : 0);
        odInfo.setSettlementOrder(okInt != null ? okInt : 0);
        odInfo.setDetails(tbod);
        return odInfo;
    }

    @Override
    public int updateByPrimaryKeySelective(TbOrderDetails tbODs) {
        return tbOrderDetailsMapper.updateByPrimaryKeySelective(tbODs);
    }

    @Override
    public int insertSelective(TbOrderDetails tbODs) {
        return tbOrderDetailsMapper.insertSelective(tbODs);
    }

    @Override
    public MaxMinCreateTime allTkStatusPayment() {
        return tbOrderDetailsMapper.allTkStatusPayment();
    }
}
