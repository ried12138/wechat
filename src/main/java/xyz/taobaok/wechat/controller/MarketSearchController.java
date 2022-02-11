package xyz.taobaok.wechat.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.taobaok.wechat.service.MarketSearchService;
import xyz.taobaok.wechat.toolutil.R;

/**
 * 搜索页
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2022/2/11   17:49
 * @Version 1.0
 */
@Api("搜索页")
@Slf4j
@RestController
@RequestMapping("/search")
public class MarketSearchController {


    @Autowired
    MarketSearchService marketSearchService;


    @ApiOperation("热门搜索，热词")
    @GetMapping("/hotword")
    public R hotword(){
        return marketSearchService.getHotword();
    }
}
