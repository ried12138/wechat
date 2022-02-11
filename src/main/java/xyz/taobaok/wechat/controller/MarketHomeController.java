package xyz.taobaok.wechat.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.taobaok.wechat.service.MarketHomeService;
import xyz.taobaok.wechat.toolutil.R;

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
    public R homeActivityBanner(){
        return marketHomeService.getActivityBanner();
    }

    @ApiOperation("限时抢购")
    @GetMapping("/flashSale")
    public R homeFlashSale(){
        return marketHomeService.getHomeFlashSale();
    }


}
