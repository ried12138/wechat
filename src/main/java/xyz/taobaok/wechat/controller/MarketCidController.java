package xyz.taobaok.wechat.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.taobaok.wechat.service.MarketCidService;
import xyz.taobaok.wechat.toolutil.R;

import java.util.List;

/**
 * 商品分类
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2022/2/14   15:25
 * @Version 1.0
 */
@Api("商品分类")
@Slf4j
@RestController
@RequestMapping("/category")
public class MarketCidController {

    @Autowired
    MarketCidService marketCidService;


    @ApiOperation("商品级别分类")
    @GetMapping("/cid")
    public R cid(){
        return marketCidService.getCid();
    }
}
