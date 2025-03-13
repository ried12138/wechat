package xin.pwdkeeper.wechat.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import xin.pwdkeeper.wechat.bean.DictItem;
import xin.pwdkeeper.wechat.bean.DictType;
import xin.pwdkeeper.wechat.service.DictItemService;
import xin.pwdkeeper.wechat.service.DictTypeService;
import xin.pwdkeeper.wechat.service.RedisService;
import xin.pwdkeeper.wechat.util.RedisKeysUtil;

import javax.annotation.PostConstruct;
import java.util.List;

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