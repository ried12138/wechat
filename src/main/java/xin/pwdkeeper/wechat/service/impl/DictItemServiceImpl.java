package xin.pwdkeeper.wechat.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xin.pwdkeeper.wechat.bean.DictItem;
import xin.pwdkeeper.wechat.mapper.DictItemMapper;
import xin.pwdkeeper.wechat.service.DictItemService;

import java.util.List;

@Service
public class DictItemServiceImpl implements DictItemService {
    @Autowired
    private DictItemMapper dictItemMapper;

    @Override
    public void addDictItem(DictItem dictItem) {
        dictItemMapper.insert(dictItem);
    }

    @Override
    public DictItem getDictItemById(int itemId) {
        return dictItemMapper.selectById(itemId);
    }

    @Override
    public List<DictItem> getAllDictItems() {
        return dictItemMapper.selectAll();
    }

    @Override
    public void updateDictItem(DictItem dictItem) {
        dictItemMapper.update(dictItem);
    }

    @Override
    public void deleteDictItem(int itemId) {
        dictItemMapper.delete(itemId);
    }
}