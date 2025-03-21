//package xyz.taobaok.wechat.toolutil;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import xyz.taobaok.wechat.bean.UserWallet;
//import xyz.taobaok.wechat.bean.WechatUserInfo;
//import xyz.taobaok.wechat.bean.dataoke.JdOrderDetails;
//import xyz.taobaok.wechat.bean.dataoke.OrderConstant;
//import xyz.taobaok.wechat.bean.dataoke.TbOrderDetails;
//import xyz.taobaok.wechat.mapper.JdOrderDetailsMapper;
//import xyz.taobaok.wechat.mapper.TbOrderDetailsMapper;
//import xyz.taobaok.wechat.mapper.UserWalletMapper;
//import xyz.taobaok.wechat.mapper.WechatUserInfoMapper;
//
//import java.math.BigDecimal;
//import java.util.Date;
//import java.util.List;
//
///**
// * 钱包信息更新
// * @Author weiranliu
// * @Email liuweiran12138@outlook.com
// * @Date 2021/6/17   3:14 下午
// * @Version 1.0
// */
//
//@Slf4j
//@Component
//@Service
//public class WalletService {
//
//
//    @Autowired
//    WechatUserInfoMapper wechatUserMapper;
//    @Autowired
//    TbOrderDetailsMapper tbODMapper;
//    @Autowired
//    JdOrderDetailsMapper jdODMapper;
//    @Autowired
//    UserWalletMapper userWalletMapper;
//
//
//    /**
//     * 每一小时拉取
//     * 按照订单更新时间查询
//     */
//
//    @Transactional
//    public UserWallet walletTask(String fromUserName){
//        WechatUserInfo userInfo = null;
//        UserWallet userWallet = null;
//        if (fromUserName != null){
//            userInfo = wechatUserMapper.selectBySpecialFromUserName(fromUserName);
//            userWallet = userWalletMapper.queryUserWalletInfo(userInfo.getOpenId());
//        }
//        if (userInfo !=null){
//            //淘宝：获取 已结算、已付款、未返利的订单
//            List<TbOrderDetails> tbOrderDetails = tbODMapper.selectByRebateOrder(userInfo.getSpecialId(), OrderConstant.REBATE_STATUS);
//            //预估收益
//            BigDecimal pubShareFee = userWallet.getPubShareFee();
//            //余额
//            BigDecimal balance = userWallet.getBalance();
//            for (TbOrderDetails tbOrderDetail : tbOrderDetails) {
//                if (tbOrderDetail.getTkStatus() ==OrderConstant.ORDER_STATUS_SUCCESS && tbOrderDetail.getStatus() == OrderConstant.REBATE_STATUS){
//                    balance.add(tbOrderDetail.getPubShareFee());
//                }else{
//                    pubShareFee.add(tbOrderDetail.getPubShareFee());
//                }
//            }
//            //京东: 已付款、确认收货、未返利的订单
//            List<JdOrderDetails> jdOrderDetails = jdODMapper.selectByPrimarySubUnionId(userInfo.getSpecialId(), OrderConstant.REBATE_STATUS);
//            for (JdOrderDetails jdOrderDetail : jdOrderDetails) {
//                if (jdOrderDetail.getValidcode() == OrderConstant.JD_ORDER_STATUS_RECEIV && jdOrderDetail.getStatus() == OrderConstant.REBATE_STATUS){
//                    balance.add(jdOrderDetail.getActualcosprice());
//                }else{
//                    pubShareFee.add(jdOrderDetail.getEstimateCosPrice());
//                }
//            }
//            userWallet.setUpdateTime(new Date());
//            if (userWalletMapper.updateByPrimaryKeySelective(userWallet) == 1){
//                for (TbOrderDetails tbOrderDetail : tbOrderDetails) {
//                    if (tbOrderDetail.getTkStatus() ==OrderConstant.ORDER_STATUS_SUCCESS && tbOrderDetail.getStatus() == OrderConstant.REBATE_STATUS){
//                        tbOrderDetail.setStatus(1);
//                        tbOrderDetail.setUpdateTime(new Date());
//                        if (tbODMapper.updateByPrimaryKeySelective(tbOrderDetail) == 1){
//                            log.info("微信："+userInfo.getFromusername()+"，订单编号："+tbOrderDetail.getTradeParentId()+" 返利成功");
//                        }else{
//                            //钱包入账失败！ 退还佣金
//                            BigDecimal balance1 = userWallet.getBalance();
//                            balance1.subtract(tbOrderDetail.getPubShareFee());
//                            userWallet.setBalance(balance1);
//                            userWallet.setUpdateTime(new Date());
//                            if (userWalletMapper.updateByPrimaryKeySelective(userWallet) == 1){
//                                log.error("微信："+userInfo.getFromusername()+"，订单编号："+tbOrderDetail.getTradeParentId()+" 返利失败");
//                            }
//                        }
//                    }
//                }
//                for (JdOrderDetails jdOrderDetail : jdOrderDetails) {
//                    if (jdOrderDetail.getValidcode() == OrderConstant.JD_ORDER_STATUS_RECEIV && jdOrderDetail.getStatus() == OrderConstant.REBATE_STATUS){
//                        jdOrderDetail.setStatus(1);
//                        jdOrderDetail.setUpdateTime(new Date());
//                        if (jdODMapper.updateByPrimaryKeySelective(jdOrderDetail) == 1){
//                            log.info("微信："+userInfo.getFromusername()+"，淘宝订单编号："+jdOrderDetail.getId()+" 返利成功");
//                        }else{
//                            //钱包入账失败！ 退还佣金
//                            BigDecimal balance1 = userWallet.getBalance();
//                            balance1.subtract(jdOrderDetail.getActualcosprice());
//                            userWallet.setBalance(balance1);
//                            userWallet.setUpdateTime(new Date());
//                            if (userWalletMapper.updateByPrimaryKeySelective(userWallet) == 1){
//                                log.error("微信："+userInfo.getFromusername()+"，京东订单编号："+jdOrderDetail.getId()+" 返利失败");
//                            }
//                        }
//                        balance.add(jdOrderDetail.getActualcosprice());
//                    }
//                }
//                log.info("微信："+userInfo.getFromusername()+"，用户钱包更新成功！");
//            }
//        }
//        return userWallet;
//    }
//}
