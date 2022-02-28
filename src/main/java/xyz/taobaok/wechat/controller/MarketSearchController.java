package xyz.taobaok.wechat.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.taobaok.wechat.bean.dataoke.SearchWord;
import xyz.taobaok.wechat.bean.dataoke.ShopImteList;
import xyz.taobaok.wechat.bean.dataoke.TaobaoItem;
import xyz.taobaok.wechat.service.MarketSearchService;
import xyz.taobaok.wechat.toolutil.R;

import java.util.List;

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
    public R<List<String>> hotword(){
        return marketSearchService.getHotword();
    }

    @ApiOperation("关键字搜索")
    @PostMapping("/searchWord")
    public R<List<ShopImteList>> searchWord(@RequestBody SearchWord itemTitle){
        return marketSearchService.searchWord(itemTitle);
    }
    @ApiOperation("获取商品详情")
    @GetMapping("/item")
    public R<TaobaoItem> item(String itemId){
        return marketSearchService.getItem(itemId);
    }



}
