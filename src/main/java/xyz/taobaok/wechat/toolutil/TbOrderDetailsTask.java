package xyz.taobaok.wechat.toolutil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import xyz.taobaok.wechat.bean.dataoke.DtktResponse;
import xyz.taobaok.wechat.bean.dataoke.TbOrderConstant;
import xyz.taobaok.wechat.bean.dataoke.TbOrderDetails;
import xyz.taobaok.wechat.mapper.TbOrderDetailsMapper;
import xyz.taobaok.wechat.service.serviceImpl.DtkApiService;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 订单定时拉取服务
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2021/4/16   4:21 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class TbOrderDetailsTask {


    @Autowired
    DtkApiService dtkApiService;

    @Autowired
    TbOrderDetailsMapper tbOrderDetailsMapper;


    /**
     *
     * @param queryType
     * @param orderScene
     * @param tkStatus
     * @param noInsert
     * @return
     */
    public List<TbOrderDetails> getTbOrderDetails(Integer queryType,Integer orderScene,Integer tkStatus,boolean noInsert){
        String endTime = DateTimeUtil.dateAddMinutes(null, 20);
        String startTime = DateTimeUtil.getNowTime_EN();
        String str = null;
        try {
            str = dtkApiService.getTbOrderDetails(queryType,orderScene,startTime,endTime,tkStatus);
        } catch (UnsupportedEncodingException e) {
            log.error("大淘客拉取订单接口访问失败！！！");
        }
        if(str.contains("成功")){
            JSONObject jsonObject = JSON.parseObject(str);
            JSONObject data = jsonObject.getJSONObject("data");
            JSONObject results = data.getJSONObject("results");
            JSONArray json = JSONArray.parseArray(results.getString("publisher_order_dto"));
            return getIntegration(json);
        }
        return null;
    }
    /**
     * 淘宝订单拉取
     * 每20分钟计统计一次
     * @param
     * @return
     */
//    @Scheduled(cron = "0 0/20 * * * ?")
//    @Scheduled(cron = "0/5 * * * * ?")
    private void getTbOrderDetails(){
        String endTime = DateTimeUtil.dateAddMinutes(null, 20);
        String startTime = DateTimeUtil.getNowTime_EN();
        String str = null;
        try {
            //付款时间查询
            str = dtkApiService.getTbOrderDetails(TbOrderConstant.PAYMENT_TIME_QUERY,
                    TbOrderConstant.ORDER_SCENARIO_MEMBER,startTime,endTime,TbOrderConstant.ORDER_STATUS_PAYMENT);
        } catch (UnsupportedEncodingException e) {
            log.error("大淘客拉取订单接口访问失败！！！");
        }
        if(str.contains("成功")){
            JSONObject jsonObject = JSON.parseObject(str);
            JSONObject data = jsonObject.getJSONObject("data");
            JSONObject results = data.getJSONObject("results");
            JSONArray json = JSONArray.parseArray(results.getString("publisher_order_dto"));
            getIntegration(json);
        }
    }

    /**
     * 封装订单对象
     * @param jsonObject
     * @return
     */
    private List<TbOrderDetails> getIntegration(JSONArray jsonObject){
        List<TbOrderDetails> list = new ArrayList<>();
        for (int i = 0 ;i<jsonObject.size();i++){
            JSONObject json = jsonObject.getJSONObject(i);
            TbOrderDetails tbODs = new TbOrderDetails();
            Date tbPaidTime = null;
            BigDecimal pay_price = null;
            BigDecimal pub_share_fee = null;
            Date tk_earning_time = null;
            BigDecimal tk_total_rate = null;
            Integer tk_status = null;
            try {
                tbPaidTime = DateTimeUtil.getDefineyyyyMMddHHmmss(json.getString("tb_paid_time"));
                pay_price = new BigDecimal(json.getString("pay_price"));
                pub_share_fee = new BigDecimal(json.getString("pub_share_fee"));
                tk_earning_time = DateTimeUtil.getDefineyyyyMMddHHmmss(json.getString("tk_earning_time"));
                tk_total_rate = new BigDecimal(json.getString("tk_total_rate"));
                tk_status = Integer.valueOf(json.getString("tk_status"));
            } catch (ParseException e) {
                log.error("参数转换错误，订单信息：{}",json.toJSONString());
                return null;
            }
            String special_id = json.getString("special_id");
            //订单编号
            String trade_parent_id = json.getString("trade_parent_id");
            TbOrderDetails tbOrderDetails = tbOrderDetailsMapper.selectByPrimaryKey(trade_parent_id);
            if (tbOrderDetails != null){
                continue;
            }
            String pub_share_rate = json.getString("pub_share_rate");
            String refund_tag = json.getString("refund_tag");
            String item_title = json.getString("item_title");
            String item_id = json.getString("item_id");
            String item_link = json.getString("item_link");
            String total_commission_fee = json.getString("total_commission_fee");
            tbODs.setTbPaidTime(tbPaidTime);
            tbODs.setPayPrice(pay_price);
            tbODs.setTradeParentId(trade_parent_id);
            tbODs.setPubShareFee(pub_share_fee);
            tbODs.setTkEarningTime(tk_earning_time);
            tbODs.setPubShareRate(pub_share_rate);
            tbODs.setRefundTag(refund_tag);
            tbODs.setTkTotalRate(tk_total_rate);
            tbODs.setItemTitle(item_title);
            tbODs.setTkStatus(tk_status);
            tbODs.setItemId(item_id);
            tbODs.setItemLink(item_link);
            tbODs.setTotalCommissionFee(total_commission_fee);
            tbODs.setSpecialId(special_id);
            int check = 0;
            try {
                check = tbOrderDetailsMapper.insertSelective(tbODs);
            } catch (Exception e) {
                log.error("插入订单信息失败mysql数据库故障！！！{}",tbODs.toString());
                return null;
            }
            if (check <= 0){
                return null;
            }
            list.add(tbODs);
        }
        return list;
    }
}
