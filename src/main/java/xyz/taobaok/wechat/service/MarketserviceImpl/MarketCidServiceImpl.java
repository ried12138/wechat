package xyz.taobaok.wechat.service.MarketserviceImpl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.taobaok.wechat.bean.dataoke.ImteType;
import xyz.taobaok.wechat.service.MarketCidService;
import xyz.taobaok.wechat.service.serviceImpl.DtkApiService;
import xyz.taobaok.wechat.toolutil.R;

import java.util.List;

/**
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2022/2/14   15:29
 * @Version 1.0
 */
@Slf4j
@Service
public class MarketCidServiceImpl implements MarketCidService {

    @Autowired
    DtkApiService dtkApiService;


    /**
     * 商品分类
     * @return
     */
    @Override
    public R<List<ImteType>> getCid() {
        String json = dtkApiService.SendDaTaoKeCategory();
        if (json.contains("成功")){
            JSONObject jsonObject = JSON.parseObject(json);
            String data = jsonObject.getString("data");
            List<ImteType> list = JSONObject.parseArray(data, ImteType.class);
            return R.ok(list);
        }else{
            return R.failed(null,"拉去列表失败，请稍后再试");
        }
    }
}
