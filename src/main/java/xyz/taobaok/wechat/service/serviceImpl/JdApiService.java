package xyz.taobaok.wechat.service.serviceImpl;

import com.alibaba.fastjson.JSONObject;
import com.jd.open.api.sdk.DefaultJdClient;
import com.jd.open.api.sdk.JdClient;
import com.jd.open.api.sdk.JdException;
import jd.union.open.goods.promotiongoodsinfo.query.request.UnionOpenGoodsPromotiongoodsinfoQueryRequest;
import jd.union.open.goods.promotiongoodsinfo.query.response.PromotionGoodsResp;
import jd.union.open.goods.promotiongoodsinfo.query.response.UnionOpenGoodsPromotiongoodsinfoQueryResponse;
import jd.union.open.promotion.byunionid.get.request.PromotionCodeReq;
import jd.union.open.promotion.byunionid.get.request.UnionOpenPromotionByunionidGetRequest;
import jd.union.open.promotion.byunionid.get.response.PromotionCodeResp;
import jd.union.open.promotion.byunionid.get.response.UnionOpenPromotionByunionidGetResponse;
import jd.union.open.promotion.common.get.response.UnionOpenPromotionCommonGetResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.taobaok.wechat.config.JdManager;

import java.io.UnsupportedEncodingException;
import java.util.List;

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
     *  京东获取商品详情
     *  大淘客提供链接转链
     * @param skuIds 商品id
     * @return
     * @throws JdException
     * @throws UnsupportedEncodingException
     */
    public PromotionGoodsResp SenJdApiGoods(List<String> skuIds) throws JdException, UnsupportedEncodingException {
        String skuId = String.join(",", skuIds);
        JdClient client=new DefaultJdClient(jdManager.routerUrl,"",jdManager.appKey,jdManager.appSecret);
        UnionOpenGoodsPromotiongoodsinfoQueryRequest request=new UnionOpenGoodsPromotiongoodsinfoQueryRequest();
        request.setSkuIds(skuId);
        UnionOpenGoodsPromotiongoodsinfoQueryResponse response=client.execute(request);
        if (response.getData().length > 0){
            PromotionGoodsResp datum = response.getData()[0];
            if (!datum.getGoodsName().isEmpty()){
                String resJSON = dtkApiService.SenJdApiConvertUrl(datum.getMaterialUrl());
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



    public UnionOpenPromotionByunionidGetResponse SenJdApiConvertUrl(String materialUrl) throws JdException {
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
