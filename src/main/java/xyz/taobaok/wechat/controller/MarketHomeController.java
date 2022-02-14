package xyz.taobaok.wechat.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.taobaok.wechat.bean.ActivityBanner;
import xyz.taobaok.wechat.bean.FlashSaleShop;
import xyz.taobaok.wechat.bean.dataoke.CheapSelect;
import xyz.taobaok.wechat.bean.dataoke.ShopImteList;
import xyz.taobaok.wechat.service.MarketHomeService;
import xyz.taobaok.wechat.toolutil.R;

import java.util.List;

/**
 * 电商接口首页
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2022/2/11   14:38
 * @Version 1.0
 */
@Api("领券分享折扣网首页的信息接口")
@Slf4j
@RestController
@RequestMapping("/home")
public class MarketHomeController {

    @Autowired
    MarketHomeService marketHomeService;

    @ApiOperation("活动首页banner")
    @GetMapping("/activityBanner")
    public R<List<ActivityBanner>> homeActivityBanner(){
        return marketHomeService.getActivityBanner();
    }

    @ApiOperation("限时抢购")
    @GetMapping("/flashSale")
    public R<List<FlashSaleShop>> homeFlashSale(){
        return marketHomeService.getHomeFlashSale();
    }

    @ApiOperation("9块9包邮")
    @PostMapping("/jiukuaijiulist")
    public R<List<ShopImteList>> jiukuaijiulist(@RequestBody CheapSelect cheapSelect){
        return marketHomeService.getHomeJiukuaijiulist(cheapSelect);
    }

}
