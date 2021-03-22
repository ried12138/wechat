package xyz.taobaok.wechat.bean;

import lombok.Data;

/**
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2020/7/31   6:04 下午
 * @Version 1.0
 */
@Data
public class RequestLog {
    private String labelId;             //请求唯一标示
    private String labelTime;             //时间戳
    private String method;              //方法名
    private Object requestData;          //请求体

    public RequestLog(String labelId, String labelTime, String method, Object requestData) {
        this.labelId = labelId;
        this.labelTime = labelTime;
        this.method = method;
        this.requestData = requestData;
    }
}
