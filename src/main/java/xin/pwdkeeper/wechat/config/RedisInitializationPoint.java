package xin.pwdkeeper.wechat.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import xin.pwdkeeper.wechat.bean.DictItem;
import xin.pwdkeeper.wechat.bean.DictType;
import xin.pwdkeeper.wechat.service.DictItemService;
import xin.pwdkeeper.wechat.service.DictTypeService;
import xin.pwdkeeper.wechat.customizeService.RedisService;
import xin.pwdkeeper.wechat.util.RedisKeysUtil;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 服务启动时，初始化redis数据，
 * 将字典信息全部放到redis中，方便后续使用
 */
@Slf4j
@Service
public class RedisInitializationPoint {

    @Value("${spring.url}")
    private String url;

    @Autowired
    private DictTypeService dictTypeService;

    @Autowired
    private DictItemService dictItemService;

    @Autowired
    private RedisService redisService;

    @PostConstruct
    public void initializeRedisData() {
        //字典类型(一级字典)
        List<DictType> allDictTypes = dictTypeService.getAllDictTypes();
        //字典分类(二级或以下字典)
        List<DictItem> allDictItems = dictItemService.getAllDictItems();
        redisService.set(RedisKeysUtil.ALL_DICT_TYPES, allDictTypes);
        redisService.set(RedisKeysUtil.ALL_DICT_ITEMS, allDictItems);

        // 按 typeId 分组存储 DictItem 数据
        Map<Integer, List<DictItem>> dictItemsByTypeIdMap = allDictItems.stream()
                .collect(Collectors.groupingBy(DictItem::getTypeId));
        for (Map.Entry<Integer, List<DictItem>> entry : dictItemsByTypeIdMap.entrySet()) {
            Integer typeId = entry.getKey();
            if (typeId != 2){
                List<DictItem> dictItemsByTypeId = entry.getValue();
                String key = RedisKeysUtil.ALL_DICT_ITEMS + ":" + typeId;
                redisService.set(key, dictItemsByTypeId);
            }
        }
        //获取字典分类type_id = 2 的值，请求平台信息
        for (DictItem dictItem : allDictItems) {
            if (dictItem.getTypeId() == 2) {
                redisService.set(RedisKeysUtil.PLATFORM_INFO + dictItem.getItemCode(), dictItem.getItemValue());
            }
        }
        log.info("redis字典数据初始化完成:::::::::::::::::::::::::::::::::::::::::::::::::");
        //**************************************************************************
        redisService.set(RedisKeysUtil.SPRING_URL,url);
        log.info("redis定制url初始化完成::::::::::::::::::::::::::::::::::::::::::::::::::");
    }
}