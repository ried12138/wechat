package xyz.taobaok.wechat.service.MarketserviceImpl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.taobaok.wechat.bean.dataoke.SearchWord;
import xyz.taobaok.wechat.bean.dataoke.ShopImteList;
import xyz.taobaok.wechat.bean.dataoke.TaobaoItem;
import xyz.taobaok.wechat.service.MarketSearchService;
import xyz.taobaok.wechat.service.serviceImpl.DtkApiService;
import xyz.taobaok.wechat.toolutil.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 搜索页服务
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2022/2/11   17:51
 * @Version 1.0
 */
@Slf4j
@Service
public class MarketSearchServiceImpl implements MarketSearchService {

    @Autowired
    DtkApiService dtkApiService;

    @Override
    public R<List<String>> getHotword() {
        String json = dtkApiService.SendDaTaoKeApiTop();
        if (!json.contains("成功")){
            return R.failed(null,"未知错误，请联系管理员");
        }
        JSONObject jsonObject = JSON.parseObject(json);
        String data = jsonObject.getString("data");
        JSONObject jsonObject1 = JSON.parseObject(data);
        String hotWords = jsonObject1.getString("hotWords");
        List<String> str = JSONObject.parseObject(hotWords, List.class);
        return R.ok(str);
    }

    /**
     * 关键字搜索商品信息
     * @return
     */
    @Override
    public R<List<ShopImteList>> searchWord(SearchWord itemTitle) {
        String json = dtkApiService.SendDaTaoKeListSuperGoods(itemTitle);
        if (!json.contains("成功")){
            return R.failed(null,"获取商品失败，请重试");
        }
        JSONObject jsonObject = JSON.parseObject(json);
        String data = jsonObject.getString("data");
        JSONObject jsonObject1 = JSON.parseObject(data);
        String list= jsonObject1.getString("list");
        List<ShopImteList> shopList = JSONObject.parseArray(list, ShopImteList.class);
        return R.ok(shopList);
    }

    /**
     * 获取商品详情
     * @param itemId
     * @return
     */
    @Override
    public R<TaobaoItem> getItem(String itemId) {
        String json = dtkApiService.SenDaTaoKeApiGoods(itemId);
        if (!json.contains("成功")){
            return R.failed(null,"拉去商品详情失败，请联系管理员");
        }else {
            JSONObject jsonObject = JSONObject.parseObject(json);
            String data = jsonObject.getString("data");
            TaobaoItem list = JSONObject.parseObject(data, TaobaoItem.class);
            String s = list.getImgs().get(0);
            String[] split = s.split(",");
            List<String> imgs = new ArrayList<>();
            for (String s1 : split) {
                imgs.add(s1);
            }
            list.setImgs(imgs);
            return R.ok(list);
        }
    }
}
