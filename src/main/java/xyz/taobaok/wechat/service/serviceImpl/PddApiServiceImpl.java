package xyz.taobaok.wechat.service.serviceImpl;

import com.pdd.pop.sdk.http.PopClient;
import com.pdd.pop.sdk.http.PopHttpClient;
import com.pdd.pop.sdk.http.api.pop.request.PddDdkCashgiftDataQueryRequest;
import com.pdd.pop.sdk.http.api.pop.request.PddDdkGoodsDetailRequest;
import com.pdd.pop.sdk.http.api.pop.request.PddDdkGoodsSearchRequest;
import com.pdd.pop.sdk.http.api.pop.response.PddDdkGoodsDetailResponse;
import com.pdd.pop.sdk.http.api.pop.response.PddDdkGoodsSearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.taobaok.wechat.config.PddManager;

import java.util.List;

/**
 * 拼多多服务接口
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2021/3/2   7:15 下午
 * @Version 1.0
 */
@Service
public class PddApiServiceImpl {

    @Autowired
    PddManager pddManager;

    /**
     * 拼多多获取商品详情
     * @param goodsSign
     * @return
     * @throws Exception
     */
    public PddDdkGoodsDetailResponse.GoodsDetailResponseGoodsDetailsItem senPindDuoDuoApiGoods(String goodsSign) throws Exception {
        PopClient client = new PopHttpClient(pddManager.appKey, pddManager.appSecret);
        PddDdkGoodsSearchRequest request = new PddDdkGoodsSearchRequest();
//        request.setCustomParameters('str');
        request.setKeyword("干脆面");
        request.setCustomParameters("{\"uid\":\"11111\",\"sid\":\"22222\"}");
        request.setPid(pddManager.pid);
//        request.setPid('str');
//        request.setSearchId('str');
//        request.setZsDuoId(0L);
        PddDdkGoodsSearchResponse pddDdkGoodsSearchResponse = client.syncInvoke(request);
        PddDdkGoodsSearchResponse.GoodsSearchResponse goodsSearchResponse = pddDdkGoodsSearchResponse.getGoodsSearchResponse();
        List<PddDdkGoodsSearchResponse.GoodsSearchResponseGoodsListItem> goodsList = goodsSearchResponse.getGoodsList();
        return null;
    }


}
