package xin.pwdkeeper.wechat.mapper;

import org.apache.ibatis.annotations.Mapper;
import xin.pwdkeeper.wechat.bean.DictItem;

import java.util.List;

@Mapper
public interface DictItemMapper {
    void insert(DictItem dictItem);
    DictItem selectById(int itemId);
    List<DictItem> selectAll();
    void update(DictItem dictItem);
    void delete(int itemId);
    List<Integer> selectAllGroupByTypeId();
    List<DictItem> selectByTypeIds(Integer typeId);
}