package xin.pwdkeeper.wechat.bean;

import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

/**
 * 微信用户信息实体类
 */
@Data
public class WechatUserInfo {
    // 主键
    private Integer id;
    // 微信用户唯一标识openId
    private String userOpenId;
    // 微信名
    private String userName;
    // 手机号
    private String phone;
    // 邮箱地址
    private String email;
    // 是否处于关注公众号的状态 0=没关注 1=关注
    private Integer followStatus;
    // 创建时间
    private Date createTime;

}
