package xin.pwdkeeper.wechat.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import xin.pwdkeeper.wechat.bean.DictItem;
import xin.pwdkeeper.wechat.bean.R;
import xin.pwdkeeper.wechat.customizeService.RedisService;
import xin.pwdkeeper.wechat.mapper.DictItemMapper;
import xin.pwdkeeper.wechat.service.ChatOPenAIService;
import xin.pwdkeeper.wechat.service.DictItemService;
import xin.pwdkeeper.wechat.util.RedisKeysUtil;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class DictItemServiceImpl implements DictItemService {
    @Autowired
    private DictItemMapper dictItemMapper;

    @Autowired
    private ChatOPenAIService chatOPenAIService;

    @Autowired
    private RedisService redisService;

    @Override
    public R addDictItem(DictItem dictItem) {
        String itemValue = dictItem.getItemValue();
        //typeId = 1   账号所属平台
        List<DictItem> dictItems = dictItemMapper.selectAllByTypeId(1);
        if (itemValue.length() >= 2) {
            // 截取从开头到倒数第3个字符（即去掉最后两个字符）
            String result = itemValue.substring(0, itemValue.length() - 2);
            boolean flag = dictItems.stream()
                    .anyMatch(p -> p.getItemValue() != null && p.getItemValue().equals(result));
            if (flag) {
                return R.failed(null, itemValue + "平台项已存在");
            }
        }
        // 异步调用 AI 服务
        asyncProcessAI(dictItem, dictItems);
        return R.ok("请求已提交，正在处理中");
    }
    @Async
    public void asyncProcessAI(DictItem dictItem, List<DictItem> dictItems) {
        R r = chatOPenAIService.chatCompletion(dictItem.acquireAiByAnalyze());
        log.info("deepseek返回的内容", r);
        if (r.getCode() == 0) {
            dictItem = dictItems.get(0);
            Map<String, Object> data = (Map<String, Object>) r.getData();
            dictItem.analyseAIresults(data.get("json").toString());
            log.info("解析json后对象数据:", dictItem);
            dictItemMapper.insert(dictItem);
            //同步到redis缓存
            cacheFlushPlatformDictionary();
        }
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

    @Override
    public List<DictItem> selectByTypeIds(Integer typeId) {
        return dictItemMapper.selectByTypeIds(typeId);
    }

    @Override
    public List<Integer> selectAllGroupByTypeId() {
        return dictItemMapper.selectAllGroupByTypeId();
    }


    private void cacheFlushPlatformDictionary() {
        //
        List<DictItem> allDictItems = selectByTypeIds(1);
        String key = RedisKeysUtil.ALL_DICT_ITEMS + ":" + allDictItems.get(0).getTypeId();
        redisService.set(key, allDictItems);
        log.info("缓存刷新完成: key={}, 数据={}", key, allDictItems);
    }
}