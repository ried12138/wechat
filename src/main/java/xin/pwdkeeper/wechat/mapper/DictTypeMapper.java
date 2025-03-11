package xin.pwdkeeper.wechat.mapper;

import org.apache.ibatis.annotations.Mapper;
import xin.pwdkeeper.wechat.bean.DictType;

import java.util.List;

@Mapper
public interface DictTypeMapper {
    void insert(DictType dictType);
    DictType selectById(int typeId);
    List<DictType> selectAll();
    void update(DictType dictType);
    void delete(int typeId);
}