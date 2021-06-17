package xyz.taobaok.wechat.service.serviceImpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.taobaok.wechat.bean.UserWallet;
import xyz.taobaok.wechat.bean.WechatUserInfo;
import xyz.taobaok.wechat.bean.dataoke.JdOrderDetails;
import xyz.taobaok.wechat.bean.dataoke.OrderConstant;
import xyz.taobaok.wechat.bean.dataoke.TbOrderDetails;
import xyz.taobaok.wechat.mapper.JdOrderDetailsMapper;
import xyz.taobaok.wechat.mapper.TbOrderDetailsMapper;
import xyz.taobaok.wechat.mapper.UserWalletMapper;
import xyz.taobaok.wechat.mapper.WechatUserInfoMapper;
import xyz.taobaok.wechat.service.UserWalletService;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 钱包服务逻辑
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2021/4/23   7:32 下午
 * @Version 1.0
 */
@Slf4j
@Service
public class UserWalletServiceImpl implements UserWalletService {

    @Autowired
    UserWalletMapper userWalletMapper;
    @Autowired
    WechatUserInfoMapper wechatUserMapper;
    @Autowired
    TbOrderDetailsMapper tbODMapper;
    @Autowired
    JdOrderDetailsMapper jdODMapper;



    @Override
    public int insertSelective(UserWallet userWallet) {
        return userWalletMapper.insertSelective(userWallet);
    }

    @Transactional
    @Override
    public UserWallet queryUserWalletInfo(String fromUserName) {
        WechatUserInfo userInfo = null;
        UserWallet userWallet = null;
        if (fromUserName != null){
            userInfo = wechatUserMapper.selectBySpecialFromUserName(fromUserName);
            userWallet = userWalletMapper.queryUserWalletInfo(userInfo.getOpenId());
        }
        if (userInfo !=null){
            //淘宝：获取 已结算、已付款、未返利的订单
            List<TbOrderDetails> tbOrderDetails = tbODMapper.selectByRebateOrder(userInfo.getSpecialId(), OrderConstant.REBATE_STATUS);
            //预估收益
            BigDecimal pubShareFee = userWallet.getPubShareFee();
            //余额
            BigDecimal balance = userWallet.getBalance();
            //累计金额
            BigDecimal cumulationIncome = userWallet.getCumulationIncome();
            for (TbOrderDetails tbOrderDetail : tbOrderDetails) {
                if (tbOrderDetail.getTkStatus() ==OrderConstant.ORDER_STATUS_SUCCESS && tbOrderDetail.getStatus() == OrderConstant.REBATE_STATUS){
                    balance = balance.add(tbOrderDetail.getPubShareFee());
                    cumulationIncome = cumulationIncome.add(tbOrderDetail.getPubShareFee());
                }else{
                    pubShareFee = pubShareFee.add(tbOrderDetail.getPubShareFee());
                    tbOrderDetail.setStatus(2);
                }
            }
            //京东: 已付款、确认收货、未返利的订单
            List<JdOrderDetails> jdOrderDetails = jdODMapper.selectByPrimarySubUnionId(userInfo.getSpecialId(), OrderConstant.REBATE_STATUS);
            for (JdOrderDetails jdOrderDetail : jdOrderDetails) {
                if (jdOrderDetail.getValidcode() == OrderConstant.JD_ORDER_STATUS_RECEIV && jdOrderDetail.getStatus() == OrderConstant.REBATE_STATUS){
                    balance = balance.add(jdOrderDetail.getActualcosprice());
                    cumulationIncome.add(jdOrderDetail.getActualcosprice());
                }else{
                    pubShareFee = pubShareFee.add(jdOrderDetail.getEstimateCosPrice());
                    jdOrderDetail.setStatus(2);
                }
            }
            userWallet.setPubShareFee(pubShareFee);
            userWallet.setBalance(balance);
            userWallet.setCumulationIncome(cumulationIncome);
            userWallet.setUpdateTime(new Date());
            if (userWalletMapper.updateByPrimaryKeySelective(userWallet) == 1){
                for (TbOrderDetails tbOrderDetail : tbOrderDetails) {
                    if (tbOrderDetail.getTkStatus() ==OrderConstant.ORDER_STATUS_SUCCESS && tbOrderDetail.getStatus() == OrderConstant.REBATE_STATUS){
                        tbOrderDetail.setStatus(1);
                        tbOrderDetail.setUpdateTime(new Date());
                        if (tbODMapper.updateByPrimaryKeySelective(tbOrderDetail) == 1){
                            log.info("微信："+userInfo.getFromusername()+"，订单编号："+tbOrderDetail.getTradeParentId()+" 返利成功");
                        }else{
                            //钱包入账失败！ 退还佣金
                            BigDecimal balance1 = userWallet.getBalance();
                            balance1.subtract(tbOrderDetail.getPubShareFee());
                            userWallet.setBalance(balance1);
                            BigDecimal cumulationIncome1 = userWallet.getCumulationIncome();
                            cumulationIncome1.subtract(balance1);
                            userWallet.setCumulationIncome(cumulationIncome1);
                            userWallet.setUpdateTime(new Date());
                            if (userWalletMapper.updateByPrimaryKeySelective(userWallet) == 1){
                                log.error("微信："+userInfo.getFromusername()+"，订单编号："+tbOrderDetail.getTradeParentId()+" 返利失败");
                            }
                        }
                    }else if(tbOrderDetail.getStatus() == OrderConstant.REBATE_STATUS_N){
                        if (tbODMapper.updateByPrimaryKeySelective(tbOrderDetail) == 1){
                            log.info("微信："+userInfo.getFromusername()+"，订单编号："+tbOrderDetail.getTradeParentId()+"返利状态修改成功");
                        }
                    }
                }
                for (JdOrderDetails jdOrderDetail : jdOrderDetails) {
                    if (jdOrderDetail.getValidcode() == OrderConstant.JD_ORDER_STATUS_RECEIV && jdOrderDetail.getStatus() == OrderConstant.REBATE_STATUS){
                        jdOrderDetail.setStatus(1);
                        jdOrderDetail.setUpdateTime(new Date());
                        if (jdODMapper.updateByPrimaryKeySelective(jdOrderDetail) == 1){
                            log.info("微信："+userInfo.getFromusername()+"，淘宝订单编号："+jdOrderDetail.getId()+" 返利成功");
                        }else{
                            //钱包入账失败！ 退还佣金
                            BigDecimal balance1 = userWallet.getBalance();
                            balance1.subtract(jdOrderDetail.getActualcosprice());
                            userWallet.setBalance(balance1);
                            BigDecimal cumulationIncome1 = userWallet.getCumulationIncome();
                            cumulationIncome1.subtract(balance1);
                            userWallet.setCumulationIncome(cumulationIncome1);
                            userWallet.setUpdateTime(new Date());
                            if (userWalletMapper.updateByPrimaryKeySelective(userWallet) == 1){
                                log.error("微信："+userInfo.getFromusername()+"，京东订单编号："+jdOrderDetail.getId()+" 返利失败");
                            }
                        }
                        balance.add(jdOrderDetail.getActualcosprice());
                    }else if(jdOrderDetail.getStatus() == OrderConstant.REBATE_STATUS_N){
                        if (jdODMapper.updateByPrimaryKeySelective(jdOrderDetail) == 1){
                            log.info("微信："+userInfo.getFromusername()+"，淘宝订单编号："+jdOrderDetail.getId()+"返利状态修改成功");
                        }
                    }
                }
                log.info("微信："+userInfo.getFromusername()+"，用户钱包更新成功！");
            }else{
                log.info("微信："+userInfo.getFromusername()+"，用户钱包更新失败！");
            }
        }
        return userWallet;
    }
}
