package xyz.taobaok.wechat.bean;

import lombok.Data;

/**
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2020/7/31   7:09 下午
 * @Version 1.0
 */
@Data
public class ResponseLog {
    private String labelId;             //请求唯一标示
    private String labelTime;             //时间戳
    private String method;              //方法名
    private Object responseData;        //响应体


    public ResponseLog(String labelId, String labelTime, String method, Object responseData) {
        this.labelId = labelId;
        this.labelTime = labelTime;
        this.method = method;
        this.responseData = responseData;
    }
}
