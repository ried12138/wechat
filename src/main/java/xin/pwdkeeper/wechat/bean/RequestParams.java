package xin.pwdkeeper.wechat.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 请求参数标准对象
 * @param <T> 请求体的泛型类型
 */
@Data
public class RequestParams<T> implements Serializable {
    //请求方所属平台
    private String requestPlatFrom;
    // 请求方唯一标识
    private String requestId;
    // 用户唯一标识
    private String userId;
    // 时间戳（毫秒单位）
    private Long timestamp;
    // 请求体
    private T requestBody;
}