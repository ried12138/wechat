package xyz.taobaok.wechat.mapper;

import org.springframework.stereotype.Repository;
import xyz.taobaok.wechat.bean.dataoke.JdOrderDetails;

@Repository
public interface JdOrderDetailsMapper {
    int deleteByPrimaryKey(String id);

    int insert(JdOrderDetails record);

    int insertSelective(JdOrderDetails record);

    JdOrderDetails selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(JdOrderDetails record);

    int updateByPrimaryKey(JdOrderDetails record);
}