package xyz.taobaok.wechat.service.serviceImpl;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.taobaok.wechat.bean.dataoke.Dataa;
import xyz.taobaok.wechat.bean.dataoke.DtktResponse;
import xyz.taobaok.wechat.config.DtkManager;
import xyz.taobaok.wechat.config.JdManager;
import xyz.taobaok.wechat.toolutil.HttpUtils;
import xyz.taobaok.wechat.toolutil.SignMD5Util;

import java.io.UnsupportedEncodingException;
import java.util.TreeMap;

/**
 * 大淘客接口
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2021/2/25   10:57 下午
 * @Version 1.0
 */
@Slf4j
@Service
public class DtkApiService {



    @Autowired
    DtkManager dtkManager;
    @Autowired
    JdManager jdManager;

    /**
     * 京东商品转链
     * @param materialUrl
     * @return
     * @throws UnsupportedEncodingException
     */
    public String SenJdApiConvertUrl(String materialUrl) throws UnsupportedEncodingException {
        TreeMap<String, Object> paraMap = new TreeMap<>();
        paraMap.put("version", "v1.0.0");
        paraMap.put("appKey", dtkManager.appKey);
        paraMap.put("appSecret", dtkManager.appSecret);
        paraMap.put("unionId", jdManager.unionId);
        paraMap.put("materialId", materialUrl);
        paraMap.put("sign", SignMD5Util.getSignStr(paraMap, dtkManager.appSecret));
        return HttpUtils.sendGet(dtkManager.jdItemConvert, paraMap);
    }

    /**
     * 高效转链
     * @param id
     * @return
     * @throws UnsupportedEncodingException
     */
    public String senDaTaoKeApiLink(String id) throws UnsupportedEncodingException {
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("version", "v1.1.1");
        map.put("goodsId", id);
        TreeMap<String, Object> paraMap = getParaMap(map);
        String jsonString = HttpUtils.sendGet(dtkManager.getPrivilegeLink, paraMap);
        return jsonString;
    }

    /**
     * 获取商品详细信息
     * @param id
     * @return
     * @throws UnsupportedEncodingException
     */
    public String SenDaTaoKeApiGoods(String id) throws UnsupportedEncodingException {
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("version", "v1.2.1");
        map.put("goodsId", id);
        TreeMap<String, Object> paraMap = getParaMap(map);
        String jsonString = HttpUtils.sendGet(dtkManager.details, paraMap);
        return jsonString;
    }

    /**
     * 淘宝优惠券信息整理
     * @param title
     * @param couponJSon
     * @return
     */
    public String tbItemCouponArrange(String title, String couponJSon) {
        DtktResponse response = JSONObject.parseObject(couponJSon, DtktResponse.class);
        Dataa data = response.getData();
        StringBuffer content = new StringBuffer("");
        content.append("找到优惠券!长按复制到淘宝：" + "\n" + "商品名称：【" + title + "】\n" + "------------------\n");
        if (!data.getCouponInfo().equals("")) {
            content.append("优惠面额:" + data.getCouponInfo());
        }
        if (!data.getCouponRemainCount().equals("")) {
            content.append("\n" + "优惠券剩余数：" + data.getCouponRemainCount());
        }
        content.append("\n" + "优惠码：" + data.getTpwd());
        if (!data.getCouponEndTime().equals("")) {
            content.append("\n" + "优惠券失效时间：" + data.getCouponEndTime());
        }
        return content.toString();
    }

    /**
     * 封装请求paraMap
     * @param map   需要请求的参数
     * @return
     */
    public TreeMap<String, Object> getParaMap(TreeMap<String, Object> map){
        TreeMap<String, Object> paraMap = new TreeMap<String, Object>();
        paraMap.put("appKey", dtkManager.appKey);
        paraMap.put("pid", dtkManager.pid);
        paraMap.putAll(map);
        paraMap.put("sign", SignMD5Util.getSignStr(paraMap, dtkManager.appSecret));
        return paraMap;
    }
}
