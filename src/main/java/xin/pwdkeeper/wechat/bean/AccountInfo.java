package xin.pwdkeeper.wechat.bean;

import lombok.Data;

import java.util.Date;

/**
 * 账号信息实体类
 */
@Data
public class AccountInfo {
    // id
    private Integer id;
    // 所属用户，用户id
    private Integer userId;
    // 所属网站、平台，参考字典
    private Integer classType;
    // 账号
    private String account;
    // 被加密的密码
    private String password;
    // 存储其他信息，卡密等信息
    private String otherSecret;
    // 绑定的手机号
    private String bindPhone;
    // 绑定的邮箱
    private String bindEmail;
    // 绑定的答案
    private String bindAnswer;
    // 绑定的问题
    private String bindAsk;
    // 创建时间
    private Date creationTime;
    // 最后一次更新时间
    private Date updateTime;
    // 标记，0=未删除，1=已删除
    private Integer flag;

}
