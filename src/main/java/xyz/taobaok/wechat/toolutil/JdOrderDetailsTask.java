package xyz.taobaok.wechat.toolutil;

import com.jd.open.api.sdk.domain.kplunion.OrderService.response.query.OrderRowQueryResult;
import com.jd.open.api.sdk.domain.kplunion.OrderService.response.query.OrderRowResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import xyz.taobaok.wechat.bean.dataoke.JdOrderDetails;
import xyz.taobaok.wechat.bean.dataoke.OrderConstant;
import xyz.taobaok.wechat.mapper.JdOrderDetailsMapper;
import xyz.taobaok.wechat.service.serviceImpl.JdApiService;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;

/**
 * 京东订单查询
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2021/6/9   4:07 下午
 * @Version 1.0
 */
@Slf4j
@Component
@Service
public class JdOrderDetailsTask {

    @Autowired
    JdApiService jdApiService;

    @Autowired
    JdOrderDetailsMapper jdODMapper;


    /**
     * 每4分钟拉取
     * 下单完成
     */
    @Async
    @Scheduled(cron = "1 0/4 * * * ?")
    public void getTbOrderDetails(){
        long start = System.currentTimeMillis();
        log.info("拉取京东下单订单任务开始.....");
        String startTime= DateTimeUtil.dateAddMinutes(15);
        String endTime = DateTimeUtil.getNowTime_EN();
        boolean hasNext = true;
        int pageNo = 0;
        while (hasNext){
            pageNo++;
            OrderRowQueryResult result = null;
            try {
                result = jdApiService.jdOrderDetails(startTime, endTime, "", pageNo, OrderConstant.ORDER_PLACE_TIME);
            } catch (Exception e) {
                log.error("京东拉取订单接口访问失败！！！");
            }
            if (result != null){
                OrderRowResp[] data = result.getData();
                for(int i = 0;i < data.length;i++){
                    JdOrderDetails orderDetail = getIntegration(data[i]);
                    if (orderDetail != null){
                        try {
                            if (jdODMapper.selectByPrimaryKey(orderDetail.getId()) != null){
                                if (jdODMapper.updateByPrimaryKeySelective(orderDetail) == 1){
                                    log.info("修改一条已完成订单：{}",orderDetail.toString());
                                    continue;
                                }
                            }
                            if (jdODMapper.insertSelective(orderDetail) == 1){
                                log.info("插入一条已完成订单：{}",orderDetail.toString());
                            }
                        } catch (Exception e) {
                            log.error("插入一条已完成订单失败！连接数据库可能出现故障:{}",e);
                        }
                    }
                }
                hasNext = result.getHasMore();
            }else{
                hasNext = false;
            }
        }
        log.info("拉取下单订单完成！耗时：{} ms",System.currentTimeMillis() - start);
    }
    /**
     * 每10分钟拉取
     * 更新的订单
     */
    @Async
    @Scheduled(cron = "1 0/10 * * * ?")
    public void getJdOrderDetailsUpdate(){
        long start = System.currentTimeMillis();
        log.info("拉取京东更新状态订单任务开始.....");
        String startTime= DateTimeUtil.dateAddMinutes(17);
        String endTime = DateTimeUtil.getNowTime_EN();
        boolean hasNext = true;
        int pageNo = 0;
        while (hasNext){
            pageNo++;
            OrderRowQueryResult result = null;
            try {
                //拉取完成状态订单
                result = jdApiService.jdOrderDetails(startTime, endTime, "", pageNo, OrderConstant.ORDER_UPDATE_TIME);
            } catch (Exception e) {
                log.error("京东拉取订单接口访问失败！！！");
            }
            if (result != null){
                OrderRowResp[] data = result.getData();
                for(int i = 0;i < data.length;i++){
                    JdOrderDetails orderDetail = getIntegration(data[i]);
                    if (orderDetail != null){
                        //插入数据库 未写完
                        try {
                            if (jdODMapper.updateByPrimaryKeySelective(orderDetail) == 1){
                                log.info("修改一条订单状态：{}",orderDetail.toString());
                            }
                        } catch (Exception e) {
                            log.error("修改一条订单状态失败！连接数据库可能出现故障:{}",e);
                        }
                    }
                }
                hasNext = result.getHasMore();
            }else{
                hasNext = false;
            }
        }
        log.info("拉取订单状态完成！耗时：{} ms",System.currentTimeMillis() - start);
    }


    private JdOrderDetails getIntegration(OrderRowResp orderRow){
        JdOrderDetails jdOrderDetails = new JdOrderDetails();
        jdOrderDetails.setId(orderRow.getId());
        jdOrderDetails.setOrderid(orderRow.getOrderId());
        jdOrderDetails.setParentid(orderRow.getParentId());
        try {
            if (!orderRow.getOrderTime().equals("")){
                jdOrderDetails.setOrdertime(DateTimeUtil.getDefineyyyyMMddHHmmss(orderRow.getOrderTime()));
            }
            if (!orderRow.getFinishTime().equals("")){
                jdOrderDetails.setFinishtime(DateTimeUtil.getDefineyyyyMMddHHmmss(orderRow.getFinishTime()));
            }
            if (!orderRow.getModifyTime().equals("")){
                jdOrderDetails.setModifytime(DateTimeUtil.getDefineyyyyMMddHHmmss(orderRow.getModifyTime()));
            }
        } catch (ParseException e) {
            log.error("订单信息封装时间转换错误");
            return null;
        }
        jdOrderDetails.setPlus(orderRow.getPlus());
        jdOrderDetails.setSkuid(orderRow.getSkuId());
        jdOrderDetails.setSkuname(orderRow.getSkuName());
        jdOrderDetails.setActualcosprice(new BigDecimal(orderRow.getActualCosPrice()));
        jdOrderDetails.setEstimateCosPrice(new BigDecimal(orderRow.getEstimateCosPrice()));
        jdOrderDetails.setValidcode(orderRow.getValidCode());
        jdOrderDetails.setSubunionid(String.valueOf(orderRow.getPositionId()));
        jdOrderDetails.setPaymonth(orderRow.getPayMonth());
        jdOrderDetails.setUpdateTime(new Date());
        return jdOrderDetails;
    }
}
