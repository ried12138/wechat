package xin.pwdkeeper.wechat.service;

import xin.pwdkeeper.wechat.bean.DictItem;

import java.util.List;

public interface DictItemService {
    void addDictItem(DictItem dictItem);
    DictItem getDictItemById(int itemId);
    List<DictItem> getAllDictItems();
    void updateDictItem(DictItem dictItem);
    void deleteDictItem(int itemId);
}