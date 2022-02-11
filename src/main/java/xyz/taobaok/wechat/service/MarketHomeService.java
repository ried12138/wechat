package xyz.taobaok.wechat.service;

import xyz.taobaok.wechat.bean.ActivityBanner;
import xyz.taobaok.wechat.toolutil.R;

/**
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2022/2/11   15:52
 * @Version 1.0
 */
public interface MarketHomeService {

    R getActivityBanner();

    R getHomeFlashSale();
}
