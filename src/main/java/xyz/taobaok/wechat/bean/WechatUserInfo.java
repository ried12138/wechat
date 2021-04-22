package xyz.taobaok.wechat.bean;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * wechat_user_info
 * @author 
 */
@Data
public class WechatUserInfo implements Serializable {
    /**
     * id
     */
    private Integer id;

    /**
     * 微信账号
     */
    private String fromusername;

    /**
     * 对外开放用户标示
     */
    private String openId;

    /**
     * 淘宝绑定唯一标示sid
     */
    private String specialId;

    /**
     * 创建信息时间戳
     */
    private Date createTime;

    /**
     * 最后一次修改信息时间戳
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;


    public WechatUserInfo(String fromusername, String openId, String specialId) {
        this.fromusername = fromusername;
        this.openId = openId;
        this.specialId = specialId;
    }
}