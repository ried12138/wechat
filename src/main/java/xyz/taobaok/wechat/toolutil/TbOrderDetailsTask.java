package xyz.taobaok.wechat.toolutil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import xyz.taobaok.wechat.bean.MaxMinCreateTime;
import xyz.taobaok.wechat.bean.dataoke.OrderConstant;
import xyz.taobaok.wechat.bean.dataoke.TbOrderDetails;
import xyz.taobaok.wechat.service.TbOrderDetailsService;
import xyz.taobaok.wechat.service.serviceImpl.DtkApiService;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 淘宝订单定时拉取服务
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2021/4/16   4:21 下午
 * @Version 1.0
 */
@Slf4j
@Component
@Service
public class TbOrderDetailsTask {


    @Autowired
    DtkApiService dtkApiService;
    @Autowired
    TbOrderDetailsService tbOrderDetailsService;

    /**
     * 每一小时拉取
     * 按照订单更新时间查询
     */
    @Async
    @Scheduled(cron = "1 0 0/1 * * ?")
    public void getTbOrderDetailsHold(){
        long start = System.currentTimeMillis();
        log.info("按照订单更新时间拉取所有订单任务开始.....");
        String startTime= DateTimeUtil.dateAddMinutes(122);
        String endTime = DateTimeUtil.getNowTime_EN();
        String str = null;
        boolean hasNext = true;
        int pageNo = 0;
        while (hasNext){
            try {
                pageNo++;
                //付款时间查询
                str = dtkApiService.getTbOrderDetails(OrderConstant.UPDATE_TIME_QUERY,
                        OrderConstant.ORDER_SCENARIO_MEMBER,startTime,endTime, null,pageNo);
            } catch (UnsupportedEncodingException e) {
                log.error("大淘客拉取订单接口访问失败！！！");
            }
            if(str.contains("成功")){
                JSONObject jsonObject = JSON.parseObject(str);
                JSONObject data = jsonObject.getJSONObject("data");
                JSONObject results = data.getJSONObject("results");
                JSONArray json = JSONArray.parseArray(results.getString("publisher_order_dto"));
                getIntegration(json);
                hasNext = data.getBoolean("has_next");
            }else{
                log.info(str);
                hasNext = false;
            }
        }
        log.info("拉取订单完成！耗时：{} ms",System.currentTimeMillis() - start);
    }
    /**
     * 用户主动拉取5分钟内的订单信息
     * @param queryType
     * @param orderScene
     * @param tkStatus
     * @param noInsert
     * @return
     */
    public void getTbOrderDetails(Integer queryType,Integer orderScene,Integer tkStatus,boolean noInsert){
        String startTime = DateTimeUtil.dateAddMinutes(6);
        String endTime = DateTimeUtil.getNowTime_EN();
        String str = null;
        boolean hasNext = true;
        int pageNo = 0;
        while (hasNext) {
            try {
                pageNo++;
                str = dtkApiService.getTbOrderDetails(queryType, orderScene, startTime, endTime, tkStatus,pageNo);
            } catch (UnsupportedEncodingException e) {
                log.error("大淘客拉取订单接口访问失败！！！");
            }
            JSONObject jsonObject1 = JSONObject.parseObject(str);
            if (str.contains("成功")) {
                JSONObject jsonObject = JSON.parseObject(str);
                JSONObject data = jsonObject.getJSONObject("data");
                JSONObject results = data.getJSONObject("results");
                JSONArray json = JSONArray.parseArray(results.getString("publisher_order_dto"));
                getIntegration(json);
                hasNext = data.getBoolean("has_next");
            }else if(jsonObject1.getString("code").equals("10209")){
                log.info(str);
                hasNext = false;
            }
        }
    }
    /**
     * 淘宝订单拉取
     * 付款时间查询
     * 已付款的订单
     * 每3分钟计统计一次20分钟前的付款时间
     * @param
     * @return
     */
//    @Scheduled(cron = "0/5 * * * * ?")
    @Async
    @Scheduled(cron = "0 0/3 * * * ?")
    public void getTbOrderDetails(){
        long start = System.currentTimeMillis();
        log.info("拉取付款订单任务开始.....");
        String startTime = DateTimeUtil.dateAddMinutes(20);
        String endTime = DateTimeUtil.getNowTime_EN();
        String str = null;
        boolean hasNext = true;
        int pageNo = 0;
        while (hasNext){
            try {
                pageNo++;
                //付款时间查询
                str = dtkApiService.getTbOrderDetails(OrderConstant.PAYMENT_TIME_QUERY,
                        OrderConstant.ORDER_SCENARIO_MEMBER,startTime,endTime, OrderConstant.ORDER_STATUS_PAYMENT,pageNo);
            } catch (UnsupportedEncodingException e) {
                log.error("大淘客拉取订单接口访问失败！！！");
            }
            if(str.contains("成功")){
                JSONObject jsonObject = JSON.parseObject(str);
                JSONObject data = jsonObject.getJSONObject("data");
                JSONObject results = data.getJSONObject("results");
                JSONArray json = JSONArray.parseArray(results.getString("publisher_order_dto"));
                getIntegration(json);
                hasNext = data.getBoolean("has_next");
            }else{
                log.info(str);
                hasNext = false;
            }
        }
        log.info("拉取订单完成！耗时：{} ms",System.currentTimeMillis() - start);
    }

    /**
     * 每天凌晨3点刷新订单完成时间的订单
     * 刷新订单状态，拉取完成的订单信息进行数据库修改
     */
    @Async
    @Scheduled(cron = "0 0 3 * * ?")
    public void lcoalOrderDetailsStatusUpdate(){
        long start = System.currentTimeMillis();
        MaxMinCreateTime time = tbOrderDetailsService.allTkStatusPayment();
        if (time!=null){
            log.info("拉取结算订单任务开始.....");
            String startTime = DateTimeUtil.getDate(time.getMinTime());
            String endTime = DateTimeUtil.datereducedMinutes(time.getMinTime(), 1445);
            boolean flag = true;
            while (flag){
                Date end = null;
                try {
                    end = DateTimeUtil.getDefineyyyyMMddHHmmss(endTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                flag = DateTimeUtil.dateCompareNow(time.getMaxTime(), end);
                String str = null;
                boolean hasNext = true;
                int pageNo = 0;
                while (hasNext){
                    try {
                        pageNo++;
                        //结算时间查询，结算成功的订单
                        str = dtkApiService.getTbOrderDetails(OrderConstant.UPDATE_TIME_QUERY,
                                OrderConstant.ORDER_SCENARIO_MEMBER,startTime,endTime, null,pageNo);
                    } catch (UnsupportedEncodingException e) {
                        log.error("大淘客拉取订单接口访问失败！！！");
                    }
                    if(str.contains("成功")){
                        JSONObject jsonObject = JSON.parseObject(str);
                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONObject results = data.getJSONObject("results");
                        JSONArray json = JSONArray.parseArray(results.getString("publisher_order_dto"));
                        getIntegration(json);
                        hasNext = data.getBoolean("has_next");
                    }else{
                        log.info(str);
                        hasNext = false;
                    }
                }
                endTime = DateTimeUtil.datereducedMinutes(end, 30);
            }
            log.info("拉取订单完成！耗时：{} ms",System.currentTimeMillis() - start);
        }else{
            log.info("订单拉取结束.......没有任何订单需要更新！");
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
                String payPrice = json.getString("pay_price");
                if (payPrice !=null && !payPrice.isEmpty()){
                    pay_price = new BigDecimal(payPrice);
                }
                pub_share_fee = new BigDecimal(json.getString("pub_share_fee"));
                String tkEarningTime = json.getString("tk_earning_time");
                if (tkEarningTime != null && !tkEarningTime.isEmpty()){
                    tk_earning_time = DateTimeUtil.getDefineyyyyMMddHHmmss(tkEarningTime);
                }
                tk_total_rate = new BigDecimal(json.getString("tk_total_rate"));
                tk_status = Integer.valueOf(json.getString("tk_status"));
            } catch (ParseException e) {
                log.error("参数转换错误，订单信息：{}",json.toJSONString());
                return null;
            }
            String special_id = json.getString("special_id");
            String pub_share_rate = json.getString("pub_share_rate");
            String refund_tag = json.getString("refund_tag");
            String item_title = json.getString("item_title");
            String item_id = json.getString("item_id");
            String item_link = json.getString("item_link");
            String total_commission_fee = json.getString("total_commission_fee");
            //订单编号
            String trade_parent_id = json.getString("trade_parent_id");
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
            TbOrderDetails tbOrderDetails = tbOrderDetailsService.selectByPrimaryKey(trade_parent_id);
            int check = 0;
            if (tbOrderDetails != null){
                //订单存在，检查订单状态
                if (tk_earning_time != null && pay_price != null){
                    Date tkEarningTime = tbOrderDetails.getTkEarningTime();
                    if (tk_earning_time != null && DateTimeUtil.dateCompareNow(tk_earning_time,tkEarningTime)){
                        tbODs.setUpdateTime(new Date());
                        check = tbOrderDetailsService.updateByPrimaryKeySelective(tbODs);
                    }
                }
            }else{
                try {
                    //插入订单
                    check = tbOrderDetailsService.insertSelective(tbODs);
                } catch (Exception e) {
                    log.error("插入订单信息失败mysql数据库故障！！！{}",tbODs.toString(),e);
                    return null;
                }
            }
            if (check <= 0){
                log.info("成功插入一条订单信息：{}",JSONObject.toJSONString(tbODs));
                return null;
            }
            list.add(tbODs);
        }
        return list;
    }


}
