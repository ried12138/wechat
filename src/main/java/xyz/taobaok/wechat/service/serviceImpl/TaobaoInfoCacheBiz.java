package xyz.taobaok.wechat.service.serviceImpl;

import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.TbkTpwdCreateRequest;
import com.taobao.api.response.TbkTpwdCreateResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.taobaok.wechat.config.TbManager;

/**
 * 淘宝商品接口
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2021/2/25   10:56 下午
 * @Version 1.0
 */
@Slf4j
@Service
public class TaobaoInfoCacheBiz {

    @Autowired
    TbManager tbManager;



    public String creatTaokouling(String url){
        TaobaoClient client = new DefaultTaobaoClient(url, tbManager.getAppKey(), tbManager.getSecret());
        TbkTpwdCreateRequest req = new TbkTpwdCreateRequest();
        req.setUrl(url);
        TbkTpwdCreateResponse rsp = null;
        try {
            rsp = client.execute(req);
        } catch (ApiException e) {
            e.printStackTrace();
        }
        System.out.println(rsp.getBody());
        return "";
    }
}
