package xyz.taobaok.wechat.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 用户钱包信息
 * user_wallet
 * @author 
 */
@Data
public class UserWallet implements Serializable {
    /**
     * id
     */
    private Integer id;

    /**
     * 对外开放用户标示，关联id
     */
    private String openId;

    /**
     * 预估收入，未结算收入
     */
    private BigDecimal pubShareFee;

    /**
     * 累计收入
     */
    private BigDecimal cumulationIncome;

    /**
     * 余额，未提现金额
     */
    private BigDecimal balance;

    /**
     * 创建时间戳
     */
    private Date createTime;

    /**
     * 钱包发生变化最后一次
     */
    private Date updateTime;

    /**
     * 钱包状态，0=未知，1=正常，2=停用
     */
    private Integer status;

    /**
     * 正在提取的额度，提取中
     */
    private BigDecimal extracting;

    private static final long serialVersionUID = 1L;
}