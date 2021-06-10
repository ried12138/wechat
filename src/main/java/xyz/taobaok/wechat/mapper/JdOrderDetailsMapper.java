package xyz.taobaok.wechat.mapper;

import org.springframework.stereotype.Repository;
import xyz.taobaok.wechat.bean.dataoke.JdOrderDetails;

import java.util.List;

@Repository
public interface JdOrderDetailsMapper {
    int deleteByPrimaryKey(String id);

    int insert(JdOrderDetails record);

    int insertSelective(JdOrderDetails record);

    List<JdOrderDetails> selectByPrimarySubUnionIdInfo(String subUnionId, Integer limit);

    List<JdOrderDetails> selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(JdOrderDetails record);

    int updateByPrimaryKey(JdOrderDetails record);
}