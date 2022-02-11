package xyz.taobaok.wechat.service.MarketserviceImpl;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.taobaok.wechat.service.MarketSearchService;
import xyz.taobaok.wechat.service.serviceImpl.DtkApiService;
import xyz.taobaok.wechat.toolutil.R;

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
    public R getHotword() {
        String hotWords = dtkApiService.SendDaTaoKeApiTop();
        List<String> strings = JSONObject.parseObject(hotWords, List.class);
        return null;
    }
}
