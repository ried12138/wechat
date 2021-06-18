package xyz.taobaok.wechat.service.serviceImpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.taobaok.wechat.bean.MaxMinCreateTime;
import xyz.taobaok.wechat.bean.OrderDetailsInfo;
import xyz.taobaok.wechat.bean.dataoke.JdOrderDetails;
import xyz.taobaok.wechat.bean.dataoke.OrderConstant;
import xyz.taobaok.wechat.bean.dataoke.TbOrderDetails;
import xyz.taobaok.wechat.bean.dataoke.WechatOrderDetails;
import xyz.taobaok.wechat.mapper.JdOrderDetailsMapper;
import xyz.taobaok.wechat.mapper.TbOrderDetailsMapper;
import xyz.taobaok.wechat.service.TbOrderDetailsService;
import xyz.taobaok.wechat.toolutil.DateTimeUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
    @Autowired
    JdOrderDetailsMapper jdOrderDetailsMapper;


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
        num+= jdOrderDetailsMapper.selectByPrimarySubUnionIdCount(specialId);
        //查询已付款订单数
        Integer odInt = tbOrderDetailsMapper.selectSpecialIdtkStatus(specialId,12);
        odInt+= jdOrderDetailsMapper.selectBySubUnionIdCount(specialId, OrderConstant.JD_ORDER_STATUS_SUCCESS);
        //查询结算订单数
        Integer okInt = tbOrderDetailsMapper.selectSpecialIdtkStatus(specialId,3);
        okInt+= jdOrderDetailsMapper.selectBySubUnionIdCount(specialId, OrderConstant.JD_ORDER_STATUS_RECEIV);


        //获取淘宝近期十个订单信息
        List<TbOrderDetails> tbod = tbOrderDetailsMapper.selectByPrimarySpecialIdInfo(specialId,5);
        //获取京东近期十个订单信息
        List<JdOrderDetails> jdOrderDetails = jdOrderDetailsMapper.selectByPrimarySubUnionIdInfo(specialId, 5);
        OrderDetailsInfo odInfo = new OrderDetailsInfo();
        odInfo.setNum(num != null ? num : 0);
        odInfo.setPaymentOrder(odInt != null ? odInt : 0);
        odInfo.setSettlementOrder(okInt != null ? okInt : 0);
        List<WechatOrderDetails> list = new ArrayList<>();
//        Collections.sort(tbod, new Comparator<TbOrderDetails>() {
//            @Override
//            public int compare(TbOrderDetails tbOrderDetails, TbOrderDetails t1) {
//                if (DateTimeUtil.dateCompareNow(tbOrderDetails.getUpdateTime(),t1.getUpdateTime())){
//                    return 1;
//                }else{
//                    return 0;
//                }
//            }
//        });
//        Collections.sort(jdOrderDetails, new Comparator<JdOrderDetails>() {
//            @Override
//            public int compare(JdOrderDetails t2, JdOrderDetails t1) {
//                if (DateTimeUtil.dateCompareNow(t2.getUpdateTime(),t1.getUpdateTime())){
//                    return 1;
//                }else{
//                    return 0;
//                }
//            }
//        });
        if (tbod.size() !=0){
            for (TbOrderDetails tbOrderDetails : tbod) {
                WechatOrderDetails wechatOrderDetails = new WechatOrderDetails();
                wechatOrderDetails.setItemTitle(substring(tbOrderDetails.getItemTitle())+"...");
                wechatOrderDetails.setTradeParentId(tbOrderDetails.getTradeParentId());
                list.add(wechatOrderDetails);
            }
        }
        if (jdOrderDetails.size() != 0){
            for (JdOrderDetails jdOrderDetail : jdOrderDetails) {
                WechatOrderDetails wechatOrderDetails = new WechatOrderDetails();
                wechatOrderDetails.setItemTitle(substring(jdOrderDetail.getSkuname())+"...");
                wechatOrderDetails.setTradeParentId(String.valueOf(jdOrderDetail.getId()));
                list.add(wechatOrderDetails);
            }
        }
        odInfo.setDetails(list);
        return odInfo;
    }

    private String substring(String str){
        if (str.length() > 11){
            str = str.substring(0, 11);
        }
        return str;
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
