package xyz.taobaok.wechat.service.serviceImpl;

import com.jd.open.api.sdk.DefaultJdClient;
import com.jd.open.api.sdk.JdClient;
import com.jd.open.api.sdk.JdException;
import jd.union.open.goods.promotiongoodsinfo.query.request.UnionOpenGoodsPromotiongoodsinfoQueryRequest;
import jd.union.open.goods.promotiongoodsinfo.query.response.PromotionGoodsResp;
import jd.union.open.goods.promotiongoodsinfo.query.response.UnionOpenGoodsPromotiongoodsinfoQueryResponse;
import jd.union.open.promotion.bysubunionid.get.request.PromotionCodeReq;
import jd.union.open.promotion.bysubunionid.get.request.UnionOpenPromotionBysubunionidGetRequest;
import jd.union.open.promotion.bysubunionid.get.response.PromotionCodeResp;
import jd.union.open.promotion.bysubunionid.get.response.UnionOpenPromotionBysubunionidGetResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.taobaok.wechat.config.JdManager;

import java.util.List;

/**
 * 京东联盟api
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2021/2/27   3:44 下午
 * @Version 1.0
 */
@Service
public class JdApiService {


    @Autowired
    JdManager jdManager;

    public PromotionGoodsResp SenJdApiGoods(List<String> skuIds) throws JdException {
        String skuId = String.join(",", skuIds);
        JdClient client=new DefaultJdClient(jdManager.unionGoodsItemUrl,"",jdManager.appKey,jdManager.appSecret);
        UnionOpenGoodsPromotiongoodsinfoQueryRequest request=new UnionOpenGoodsPromotiongoodsinfoQueryRequest();
        request.setSkuIds(skuId);
        UnionOpenGoodsPromotiongoodsinfoQueryResponse response=client.execute(request);
        if (response.getData().length > 0){
            PromotionGoodsResp datum = response.getData()[0];
            if (!datum.getGoodsName().isEmpty()){
                String convertUrl = SenJdApiConvertUrl(datum.getMaterialUrl());
                datum.setMaterialUrl(convertUrl);
                return datum;
            }
        }
        return null;
    }



    public String SenJdApiConvertUrl(String materialUrl) throws JdException {
        JdClient client=new DefaultJdClient(jdManager.unionConvertUrl,"",jdManager.appKey,jdManager.appSecret);
        UnionOpenPromotionBysubunionidGetRequest request=new UnionOpenPromotionBysubunionidGetRequest();
        PromotionCodeReq promotionCodeReq=new PromotionCodeReq();
        request.setPromotionCodeReq(promotionCodeReq);
        UnionOpenPromotionBysubunionidGetResponse response = client.execute(request);
        if (response.getData() != null){
            PromotionCodeResp data = response.getData();
            if (!data.getClickURL().isEmpty()){
                return data.getClickURL();
            }else{
                return response.getMessage();
            }
        }
        return null;
    }
}
