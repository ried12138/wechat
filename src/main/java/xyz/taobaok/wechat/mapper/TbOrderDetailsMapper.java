package xyz.taobaok.wechat.mapper;

import org.springframework.stereotype.Repository;
import xyz.taobaok.wechat.bean.dataoke.TbOrderDetails;

@Repository
public interface TbOrderDetailsMapper {
    int insert(TbOrderDetails record);

    int insertSelective(TbOrderDetails record);

    TbOrderDetails selectByPrimaryKey(String tradeParentId);

    int updateByPrimaryKeySelective(TbOrderDetails record);

    int updateByPrimaryKey(TbOrderDetails record);
}