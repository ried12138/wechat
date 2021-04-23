package xyz.taobaok.wechat.mapper;

import org.springframework.stereotype.Repository;
import xyz.taobaok.wechat.bean.MaxMinCreateTime;
import xyz.taobaok.wechat.bean.OrderDetailsInfo;
import xyz.taobaok.wechat.bean.dataoke.TbOrderDetails;

import java.util.List;

@Repository
public interface TbOrderDetailsMapper {
    int insert(TbOrderDetails record);

    int insertSelective(TbOrderDetails record);

    TbOrderDetails selectByPrimaryKey(String tradeParentId);

    int updateByPrimaryKeySelective(TbOrderDetails record);

    int updateByPrimaryKey(TbOrderDetails record);

    Integer selectByPrimarySpecialId(String specialId);

    Integer selectSpecialIdtkStatus(String specialId, int tkStatus);

    List<TbOrderDetails> selectByPrimarySpecialIdInfo(String specialId, Integer limit);

    MaxMinCreateTime allTkStatusPayment();
}