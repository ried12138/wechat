package xin.pwdkeeper.wechat.controller;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xin.pwdkeeper.wechat.bean.R;
import xin.pwdkeeper.wechat.customizeService.RedisService;
import xin.pwdkeeper.wechat.util.RedisKeysUtil;

/**
 * 字典库，此控制器是对字典数据进行操作
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2025/3/14   16:04
 * @Version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/dictionary")
public class DictionarylibraryController {

    @Autowired
    private RedisService redisService;

    /**
     * 获取一级字典类型
     * @return
     */
    @GetMapping(value = "/dictType", produces = "application/json;charset=utf-8")
    public R getDictType() {
        return R.ok(redisService.get(RedisKeysUtil.ALL_DICT_TYPES));
    }

    /**
     * 获取二级以上字典类型
     * @param typeId
     * @return
     */
    @GetMapping(value = "/dictItem/{typeId}", produces = "application/json;charset=utf-8")
    @PreAuthorize("isAuthenticated()")
    public R getDictItem(@PathVariable Integer typeId) {
        return R.ok(redisService.get(RedisKeysUtil.ALL_DICT_ITEMS+":"+typeId));
    }
}
