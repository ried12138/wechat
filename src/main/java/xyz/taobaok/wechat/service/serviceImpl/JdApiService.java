package xyz.taobaok.wechat.service.serviceImpl;

import com.alibaba.fastjson.JSONObject;
import com.jd.open.api.sdk.DefaultJdClient;
import com.jd.open.api.sdk.JdClient;
import com.jd.open.api.sdk.JdException;
import com.jd.open.api.sdk.domain.kplunion.GoodsService.response.query.PromotionGoodsResp;
import com.jd.open.api.sdk.domain.kplunion.OrderService.request.query.OrderRowReq;
import com.jd.open.api.sdk.domain.kplunion.OrderService.response.query.OrderRowQueryResult;
import com.jd.open.api.sdk.request.kplunion.UnionOpenGoodsPromotiongoodsinfoQueryRequest;
import com.jd.open.api.sdk.request.kplunion.UnionOpenOrderRowQueryRequest;
import com.jd.open.api.sdk.request.kplunion.UnionOpenPromotionByunionidGetRequest;
import com.jd.open.api.sdk.response.kplunion.UnionOpenGoodsPromotiongoodsinfoQueryResponse;
import com.jd.open.api.sdk.response.kplunion.UnionOpenOrderRowQueryResponse;
import com.jd.open.api.sdk.response.kplunion.UnionOpenPromotionByunionidGetResponse;
import com.jd.open.api.sdk.domain.kplunion.promotionbyunionid.PromotionService.request.get.PromotionCodeReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.taobaok.wechat.config.JdManager;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * 京东联盟api
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2021/2/27   3:44 下午
 * @Version 1.0
 */
@Slf4j
@Service
public class JdApiService {


    @Autowired
    JdManager jdManager;
    @Autowired
    DtkApiService dtkApiService;


    /**
     *
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param key
     * @return
     */
    public OrderRowQueryResult jdOrderDetails(String startTime, String endTime, String key, int pageIndex, int type) throws Exception {
        JdClient client = new DefaultJdClient(jdManager.routerUrl,"",jdManager.appKey,jdManager.appSecret);
        UnionOpenOrderRowQueryRequest request=new UnionOpenOrderRowQueryRequest();
        OrderRowReq orderReq = new OrderRowReq();
        orderReq.setStartTime(startTime);
        orderReq.setEndTime(endTime);
        orderReq.setKey(key);
        orderReq.setPageIndex(pageIndex);
        orderReq.setPageSize(20);
        orderReq.setType(type);
        request.setOrderReq(orderReq);
        request.setVersion("1.0");
        UnionOpenOrderRowQueryResponse response=client.execute(request);
        OrderRowQueryResult queryResult = response.getQueryResult();
        if (queryResult.getCode() == 200 && queryResult.getData().length > 0){
            return queryResult;
        }
        return null;
    }



    /**
     *  京东获取商品详情
     *  大淘客提供链接转链
     * @param skuIds 商品id
     * @return
     * @throws JdException
     * @throws UnsupportedEncodingException
     */
    public PromotionGoodsResp SenJdApiGoods(List<String> skuIds, Map<String, String> parse) throws Exception {
        String skuId = String.join(",", skuIds);
        JdClient client=new DefaultJdClient(jdManager.routerUrl,"",jdManager.appKey,jdManager.appSecret);
        UnionOpenGoodsPromotiongoodsinfoQueryRequest request=new UnionOpenGoodsPromotiongoodsinfoQueryRequest();
        request.setSkuIds(skuId);
        UnionOpenGoodsPromotiongoodsinfoQueryResponse response=client.execute(request);
        if (response.getQueryResult().getData().length > 0){
            PromotionGoodsResp datum = response.getQueryResult().getData()[0];
            if (!datum.getGoodsName().isEmpty()){
                String resJSON = dtkApiService.SenJdApiConvertUrl(datum.getMaterialUrl(),parse.get("FromUserName"));
                JSONObject jsonObject = JSONObject.parseObject(resJSON);
                String msg = jsonObject.getString("msg");
                if (msg.contains("成功")){
                    JSONObject data = JSONObject.parseObject(resJSON).getJSONObject("data");
                    String convertUrl = data.getString("shortUrl").isEmpty()? data.getString("longUrl"): data.getString("shortUrl");
                    datum.setMaterialUrl(convertUrl);
                }else{
                    log.error("dtk SenJdApiConvertUrl method is error msg:{},materialUrl:{}",msg,datum.getMaterialUrl());
                }
                return datum;
            }
        }
        return null;
    }



    public UnionOpenPromotionByunionidGetResponse SenJdApiConvertUrl(String materialUrl) throws Exception {
        JdClient client=new DefaultJdClient(jdManager.routerUrl,"",jdManager.appKey,jdManager.appSecret);
        UnionOpenPromotionByunionidGetRequest request = new UnionOpenPromotionByunionidGetRequest();
        PromotionCodeReq promotionCodeReq = new PromotionCodeReq();
        promotionCodeReq.setMaterialId(materialUrl);
        promotionCodeReq.setUnionId(jdManager.unionId);
        request.setPromotionCodeReq(promotionCodeReq);
        UnionOpenPromotionByunionidGetResponse response = client.execute(request);
        return response;
    }
}
