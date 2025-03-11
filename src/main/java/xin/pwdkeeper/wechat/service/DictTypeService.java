package xin.pwdkeeper.wechat.service;

import xin.pwdkeeper.wechat.bean.DictType;

import java.util.List;

public interface DictTypeService {
    void addDictType(DictType dictType);
    DictType getDictTypeById(int typeId);
    List<DictType> getAllDictTypes();
    void updateDictType(DictType dictType);
    void deleteDictType(int typeId);
}