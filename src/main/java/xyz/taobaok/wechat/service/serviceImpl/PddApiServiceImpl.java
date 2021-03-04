package xyz.taobaok.wechat.service.serviceImpl;

import com.pdd.pop.sdk.http.PopClient;
import com.pdd.pop.sdk.http.PopHttpClient;
import com.pdd.pop.sdk.http.api.pop.request.PddDdkGoodsDetailRequest;
import com.pdd.pop.sdk.http.api.pop.response.PddDdkGoodsDetailResponse;
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
        PddDdkGoodsDetailRequest request = new PddDdkGoodsDetailRequest();
//        request.setCustomParameters('str');
        request.setGoodsSign(goodsSign);
        request.setPid(pddManager.pid);
//        request.setPid('str');
//        request.setSearchId('str');
//        request.setZsDuoId(0L);
        PddDdkGoodsDetailResponse response = client.syncInvoke(request);
        PddDdkGoodsDetailResponse.GoodsDetailResponse goodsDetailResponse = response.getGoodsDetailResponse();
        List<PddDdkGoodsDetailResponse.GoodsDetailResponseGoodsDetailsItem> goodsDetails = goodsDetailResponse.getGoodsDetails();
        PddDdkGoodsDetailResponse.GoodsDetailResponseGoodsDetailsItem goodsDetailResponseGoodsDetailsItem = goodsDetails.get(0);
        return goodsDetailResponseGoodsDetailsItem;
    }


}
