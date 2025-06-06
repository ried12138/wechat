package xin.pwdkeeper.wechat.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
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
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class DictItemServiceImpl implements DictItemService {

    @Autowired
    private DictItemMapper dictItemMapper;
    @Autowired
    private ChatOPenAIService chatOPenAIService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private RedissonClient redissonClient;

    @Override
    public R addDictItem(DictItem dictItem) {
        String itemValue = dictItem.getItemValue();
        //typeId = 1   账号所属平台
        List<DictItem> dictItems = dictItemMapper.selectAllByTypeId(1);
        String lockKey = null;
        if (itemValue.length() >= 2) {
            // 截取从开头到倒数第3个字符（即去掉最后两个字符）
            String result = itemValue.substring(0, itemValue.length());
            boolean flag = dictItems.stream()
                    .anyMatch(p -> p.getItemValue() != null && p.getItemValue().equals(result));
            if (flag) {
                return R.failed(null, itemValue + "平台已存在");
            }
            lockKey = "lock:cacheFlushPlatformDictionary" + result;
        }
        if (lockKey != null) {
            Boolean isExist = redisService.hasKey(lockKey);
            if (Boolean.TRUE.equals(isExist)){
                return R.failed(null, "已添加成功.");
            }
            RLock lock = redissonClient.getLock(lockKey);
            try {
                log.info("加锁成功，开始执行异步处理:%s"+lockKey);
                // 阻塞等待锁，
                lock.lock(20, TimeUnit.SECONDS);
                // 执行异步处理 调用 AI 服务
                asyncProcessAI(dictItem, dictItems);
                return R.ok("请求已提交，正在处理中");
            } catch (Exception e) {
                log.error("加锁或执行过程中发生异常", e);
                return R.failed(null, "系统异常，请稍后再试");
            } finally {
                if (lock.isHeldByCurrentThread()) {
                    lock.unlock(); // 仅当前线程持有锁时才释放
                }
            }
        }
        return R.ok("请求已提交，正在处理中");
    }

    @Async
    public void asyncProcessAI(DictItem dictItem, List<DictItem> dictItems) {
        R r = chatOPenAIService.chatCompletion(dictItem.acquireAiByAnalyze());
        log.info("deepseek返回的内容:%s", r);
        if (r.getCode() == 0) {
            dictItem = dictItems.get(0);
            Map<String, Object> data = (Map<String, Object>) r.getData();
            dictItem.analyseAIresults(data.get("json").toString());
            log.info("解析json后对象数据:%s", dictItem);
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