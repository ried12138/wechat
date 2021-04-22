package xyz.taobaok.wechat.service.tbserviceImpl;

import com.alibaba.fastjson.JSONObject;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.internal.util.WebUtils;
import com.taobao.api.request.TopAuthTokenCreateRequest;
import com.taobao.api.response.TopAuthTokenCreateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import xyz.taobaok.wechat.bean.taobao.TbAccessTokenResponse;
import xyz.taobaok.wechat.config.TbManager;
import xyz.taobaok.wechat.service.TaobaoApiService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * 淘宝token获取接口
 * 淘宝订单
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2021/4/15   4:45 下午
 * @Version 1.0
 */
@Service
@Configuration
@EnableScheduling
public class TaobaoApiServiceImpl implements TaobaoApiService {

    private static String defaultRefreshToken = "";

    @Autowired
    TbManager tbManager;
    @Override
    public String getAccessToken(String code,String refreshToken) throws ApiException {
        TaobaoClient client = new DefaultTaobaoClient(tbManager.getUrl(), tbManager.getAppKey(), tbManager.getSecret());
        TopAuthTokenCreateRequest req = new TopAuthTokenCreateRequest();
        req.setCode(code);
//        req.setUuid(code);
        TopAuthTokenCreateResponse rsp = client.execute(req);
        TbAccessTokenResponse response = JSONObject.parseObject(rsp.getTokenResult(), TbAccessTokenResponse.class);
        System.out.println(response.getAccessToken());
        defaultRefreshToken = response.getAccessToken();
        return response.getAccessToken();
    }

    /**
     * 定时刷新tb token值。
     */
//    @Scheduled(cron = "0 0 22/23 * * ?")
    private void refreshToken(){
        String url="https://oauth.taobao.com/token";
        Map<String,String> props=new HashMap<String,String>();
        props.put("grant_type","authorization_code");
        /*测试时，需把test参数换成自己应用对应的值*/
        props.put("code",defaultRefreshToken);  //此code值非code值，需要定时拉去
        props.put("client_id",tbManager.getAppKey());
        props.put("client_secret",tbManager.getSecret());
        props.put("redirect_uri","http://www.taobaok.xyz");
        props.put("view","web");
        String s="";
        try{s= WebUtils.doPost(url, props, 30000, 30000);
            TbAccessTokenResponse response = JSONObject.parseObject(s, TbAccessTokenResponse.class);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
