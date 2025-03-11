package xin.pwdkeeper.wechat.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xin.pwdkeeper.wechat.bean.DictType;
import xin.pwdkeeper.wechat.mapper.DictTypeMapper;
import xin.pwdkeeper.wechat.service.DictTypeService;

import java.util.List;

@Service
public class DictTypeServiceImpl implements DictTypeService {
    @Autowired
    private DictTypeMapper dictTypeMapper;

    @Override
    public void addDictType(DictType dictType) {
        dictTypeMapper.insert(dictType);
    }

    @Override
    public DictType getDictTypeById(int typeId) {
        return dictTypeMapper.selectById(typeId);
    }

    @Override
    public List<DictType> getAllDictTypes() {
        return dictTypeMapper.selectAll();
    }

    @Override
    public void updateDictType(DictType dictType) {
        dictTypeMapper.update(dictType);
    }

    @Override
    public void deleteDictType(int typeId) {
        dictTypeMapper.delete(typeId);
    }
}