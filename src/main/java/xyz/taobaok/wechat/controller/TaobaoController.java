package xyz.taobaok.wechat.controller;

import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.taobaok.wechat.service.TaobaoApiService;

/**
 * 淘宝请求服务接口
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2021/4/15   4:23 下午
 * @Version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/tb")
public class TaobaoController {

    private static final String refreshToken = "";
    @Autowired
    TaobaoApiService taobaoApiService;
    //DXFVSDFSD
    @RequestMapping(value = "/code",method = RequestMethod.GET,produces = "text/html;charset=utf-8")
    @ResponseBody
    public String index(@RequestParam("code")String code){
        log.info("tb return code is :{}",refreshToken);
        String json="";
        try {
            json = taobaoApiService.getAccessToken(code,refreshToken);
        } catch (ApiException e) {
            log.error("刷新淘宝token值发送了错误，可能是服务器请求失败导致，请检查服务！！！");
        }
        return json;
    }
}
