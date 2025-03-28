package xin.pwdkeeper.wechat.service;

import xin.pwdkeeper.wechat.bean.DictItem;
import xin.pwdkeeper.wechat.bean.R;

import java.util.List;

public interface DictItemService {
    R addDictItem(DictItem dictItem);
    DictItem getDictItemById(int itemId);
    List<DictItem> getAllDictItems();
    void updateDictItem(DictItem dictItem);
    void deleteDictItem(int itemId);
    List<DictItem> selectByTypeIds(Integer type);

    List<Integer> selectAllGroupByTypeId();
}