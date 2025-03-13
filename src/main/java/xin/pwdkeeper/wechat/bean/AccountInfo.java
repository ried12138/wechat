package xin.pwdkeeper.wechat.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * 账号信息实体类
 */
@Data
public class AccountInfo implements Serializable {
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

    /**
     * 判断账号是否为空
     * @return 如果账号为空，返回true；否则返回false
     */
    public boolean isAccountEmpty() {
        return account == null || account.isEmpty();
    }

    /**
     * 判断绑定的手机号是否为空或格式不正确
     * @return 如果绑定的手机号为空或格式不正确，返回true；否则返回false
     */
    public boolean isBindPhoneEmpty() {
        if (bindPhone == null || bindPhone.isEmpty()) {
            return true;
        }
        // 使用正则表达式验证手机号格式
        Pattern pattern = Pattern.compile("^1[3-9]\\d{9}$");
        return !pattern.matcher(bindPhone).matches();
    }

    /**
     * 判断绑定的邮箱是否为空
     * @return 如果绑定的邮箱为空，返回true；否则返回false
     */
    public boolean isBindEmailEmpty() {
        if (bindEmail == null || bindEmail.isEmpty()) {
            return true;
        }
        // 使用正则表达式验证邮箱格式
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
        return !pattern.matcher(bindEmail).matches();
    }


    /**
     * 判断账号是否已删除
     * @return 如果账号已删除，返回true；否则返回false
     */
    public boolean isDeleted() {
        return flag != null && flag == 1;
    }

    /**
     * 设置创建时间和更新时间为当前时间，如果它们为空
     */
    public void setDefaultTimes() {
        if (this.creationTime == null) {
            this.creationTime = new Date();
        }
        if (this.updateTime == null) {
            this.updateTime = new Date();
        }
    }

}