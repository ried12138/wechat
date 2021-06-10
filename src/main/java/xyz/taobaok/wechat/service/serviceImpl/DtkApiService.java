package xyz.taobaok.wechat.service.serviceImpl;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.taobaok.wechat.bean.dataoke.Dataa;
import xyz.taobaok.wechat.bean.dataoke.DtktResponse;
import xyz.taobaok.wechat.config.DtkManager;
import xyz.taobaok.wechat.config.JdManager;
import xyz.taobaok.wechat.toolutil.DateTimeUtil;
import xyz.taobaok.wechat.toolutil.HttpUtils;
import xyz.taobaok.wechat.toolutil.SignMD5Util;
import xyz.taobaok.wechat.toolutil.TpwdUtil;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.Date;
import java.util.TreeMap;

/**
 * 大淘客接口
 * http://www.dataoke.com/kfpt/api-market.html
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2021/2/25   10:57 下午
 * @Version 1.0
 */
@Slf4j
@Service
public class DtkApiService {


    @Autowired
    TaobaoInfoCacheBiz taobaoInfoCacheBiz;

    @Autowired
    DtkManager dtkManager;
    @Autowired
    JdManager jdManager;


    /**
     * 淘口令解析
     * http://www.dataoke.com/kfpt/api-d.html?id=26
     * @param tkl
     * @return
     */
    public String parseTkl(String tkl){
        tkl = TpwdUtil.headAndTailSubstitution(tkl);
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("version", "v1.0.0");
        map.put("content",tkl);
        TreeMap<String, Object> paraMap = getParaMap(map);
        return HttpUtils.sendGet(dtkManager.parseTkl, paraMap);
    }
    /**
     * 生成淘口令
     * 必须以https开头，可以是二合一链接、长链接、短链接等各种淘宝高佣链接；
     * 支持渠道备案链接。* 该参数需要进行Urlencode编码后传入*
     * http://www.dataoke.com/kfpt/api-d.html?id=28
     * @param text  口令弹框内容，长度大于5个字符
     * @param url
     * @return
     */
    public String creatTaokouling(String text,String url){
        String urlEncoderString = HttpUtils.getURLEncoderString(url);
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("version", "v1.0.0");
        map.put("text","wechata");
        map.put("url",url);
        TreeMap<String, Object> paraMap = getParaMap(map);
        paraMap.put("url",urlEncoderString);
        return HttpUtils.sendGet(dtkManager.creatTkl, paraMap);
    }

    /**
     * http://www.dataoke.com/kfpt/api-d.html?id=27
     * @param queryType 查询时间类型，1：按照订单淘客创建时间查询，2:按照订单淘客付款时间查询，3:按照订单淘客结算时间查询
     * @param orderScene 场景订单场景类型，1:常规订单，2:渠道订单，3:会员运营订单，默认为1
     * @param startTime 订单查询开始时间。时间格式：YYYY-MM-DD HH:MM:SS
     * @param endTime 订单查询结束时间。时间格式：YYYY-MM-DD HH:MM:SS
     * @param tkStatus
     * @return
     * @throws UnsupportedEncodingException
     */
    public String getTbOrderDetails(Integer queryType,Integer orderScene,String startTime,String endTime,Integer tkStatus,int pageNo) throws UnsupportedEncodingException {
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("version", "v1.0.0");
        map.put("endTime", endTime);
        map.put("startTime", startTime);
        map.put("queryType",queryType);
        map.put("orderScene",orderScene);
        map.put("tkStatus",tkStatus);
        map.put("pageNo",pageNo);
        TreeMap<String, Object> paraMap = getParaMap(map);
        return HttpUtils.sendGetTbOrderDetails(dtkManager.orderDetails, paraMap);
    }
    /**
     * 淘口令转淘口令
     * http://www.dataoke.com/kfpt/api-d.html?id=30
     * @param tkl
     * @param fromUserName
     * @return
     * @throws UnsupportedEncodingException
     */
    public String getTklConvert(String tkl, String fromUserName){
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("version", "v1.0.0");
        map.put("content",tkl);
        map.put("external_id",fromUserName);
        TreeMap<String, Object> paraMap = getParaMap(map);
        return HttpUtils.sendGet(dtkManager.tklConvert, paraMap);
    }

    /**
     * 京东商品转链
     * @param materialUrl
     * @return
     * @throws UnsupportedEncodingException
     */
    public String SenJdApiConvertUrl(String materialUrl,Long positionId) throws UnsupportedEncodingException {
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("version", "v1.0.0");
        map.put("unionId", jdManager.unionId);
        map.put("materialId", materialUrl);
        if (positionId != null && positionId != 0){
            map.put("positionId",positionId);
        }
        TreeMap<String, Object> paraMap = getParaMap(map);
        return HttpUtils.sendGet(dtkManager.jdItemConvert, paraMap);
    }

    /**
     * 高效转链
     * v1.3.1支持会员授权
     * http://www.dataoke.com/kfpt/api-d.html?id=7
     * @param itemid
     * @return
     * @throws UnsupportedEncodingException
     */
    public String senDaTaoKeApiLink(String itemid,String fromUserName) throws UnsupportedEncodingException {
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("version", "v1.3.1");
        map.put("goodsId",itemid);
        map.put("externalId",fromUserName);
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
        return HttpUtils.sendGet(dtkManager.details, paraMap);
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
        title = title == null? data.getTitle():title;
        //优惠券信息校验未完整
        //生成淘口令有问题
        String json = "";
        if (data.getCouponEndTime() != null && !data.getCouponEndTime().isEmpty()){
            try {
                Date defineyyyyMMddHH = DateTimeUtil.getDefineyyyyMMddHH(data.getCouponEndTime());
                //判断优惠券过期时间没有过期
                if (DateTimeUtil.dateCompareNow(defineyyyyMMddHH,new Date())){
                    json = creatTaokouling(title,data.getCouponClickUrl());
                }
            } catch (ParseException e) {
                log.error("优惠券日期转换失败！！！请检查是否有优惠券失效情况，itemid：{}",data.getItemId());
            }
        }else if (data.getItemUrl() != null &&!data.getItemUrl().isEmpty()){
            json = creatTaokouling(title,data.getItemUrl());
        }
        if (json.contains("成功")){
            String model = JSONObject.parseObject(json).getJSONObject("data").getString("model");
            data.setTpwd(model);
        }
        StringBuffer content = new StringBuffer("");
        content.append("找到优惠券!长按复制到淘宝：" + "\n"+"商品名称：【");
        if (title ==null){
            content.append(data.getLongTpwd().split("  ")[1]);
        }else{
            content.append(title);
        }
        content.append("】\n" + "------------------------\n");
        if (!data.getCouponInfo().equals("")) {
            content.append("优惠面额:" + data.getCouponInfo());
        }
        if (!data.getCouponTotalCount().equals("0")) {
            content.append("\n" + "优惠券总量：" + data.getCouponRemainCount());
        }
        if (!data.getCouponRemainCount().equals("0")) {
            content.append("\n" + "优惠券剩余数：" + data.getCouponRemainCount());
        }
        if (!data.getCouponEndTime().equals("")) {
            content.append("\n" + "优惠券失效时间：" + data.getCouponEndTime());
        }
        content.append("\n" + "优惠码：" + data.getTpwd());
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
