package xyz.taobaok.wechat.service.MarketserviceImpl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.taobaok.wechat.bean.ActivityBanner;
import xyz.taobaok.wechat.bean.FlashSaleShop;
import xyz.taobaok.wechat.service.MarketHomeService;
import xyz.taobaok.wechat.service.serviceImpl.DtkApiService;
import xyz.taobaok.wechat.toolutil.R;
import java.util.List;

/**
 * 电商首页service服务
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2022/2/11   15:53
 * @Version 1.0
 */
@Slf4j
@Service
public class MarketHomeServiceImpl implements MarketHomeService {

    @Autowired
    DtkApiService dtkApiService;
    /**
     * 获取banner活动信息
     * @return
     */
    @Override
    public R<List<ActivityBanner>> getActivityBanner() {
        String json = dtkApiService.senDaTaoKetbTopic(4, "1", 2);
        JSONObject jsonObject = JSON.parseObject(json);
        if (!json.contains("成功")){
            String msg = jsonObject.getString("msg");
            return R.failed(null,msg);
        }else{
            String data = jsonObject.getString("data");
            List<ActivityBanner> activityBanners = JSONObject.parseArray(data, ActivityBanner.class);
            return R.ok(activityBanners);
        }
    }

    /**
     * 限时抢购
     * @return
     */
    @Override
    public R<List<FlashSaleShop>> getHomeFlashSale() {
        String json = dtkApiService.senDaTaoKeflashSale(null);
        JSONObject jsonObject = JSON.parseObject(json);
        if (!json.contains("成功")){
            String msg = jsonObject.getString("msg");
            return R.failed(null,msg);
        }else{
            String data = jsonObject.getString("data");
            JSONObject jsonObj = JSON.parseObject(data);
            String goodsList = jsonObj.getString("goodsList");
            List<FlashSaleShop> flashSaleShops = JSONObject.parseArray(goodsList, FlashSaleShop.class);
            return R.ok(flashSaleShops);
        }
    }
}
