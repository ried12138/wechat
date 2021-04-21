package xyz.taobaok.wechat.bean.taobao;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * 参考链接：https://open.taobao.com/doc.htm?docId=102635&docType=1
 * 淘宝授权token参数对象
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2021/4/15   5:06 下午
 * @Version 1.0
 */
@Data
public class TbAccessTokenResponse {

    @JSONField(name = "access_token")
    private String accessToken;             //token值

    @JSONField(name = "token_type")
    private String tokenType;

    @JSONField(name = "expires_in")
    private String expiresIn;               //token过期时间

    @JSONField(name = "refresh_token")
    private String refreshToken;

    @JSONField(name = "re_expires_in")
    private String reExpiresIn;

    @JSONField(name = "r1_expires_in")
    private String r1ExpiresIn;

    @JSONField(name = "r2_expires_in")
    private String r2ExpiresIn;

    @JSONField(name = "w1_expires_in")
    private String w1ExpiresIn;

    @JSONField(name = "w2_expires_in")
    private String w2ExpiresIn;             //w2级别API或字段的访问过期时间

    @JSONField(name = "taobao_user_nick")
    private String taobaoUserNick;

    @JSONField(name = "taobao_user_id")
    private String taobaoUserId;

    @JSONField(name = "sub_taobao_user_id")
    private String subTaobaoUuserId;

    @JSONField(name = "sub_taobao_user_nick")
    private String subTaobaoUserNick;
}
