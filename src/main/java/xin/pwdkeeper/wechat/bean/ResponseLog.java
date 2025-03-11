package xin.pwdkeeper.wechat.bean;

import lombok.Data;

/**
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2020/7/31   7:09 下午
 * @Version 1.0
 */
@Data
public class ResponseLog {
    //请求唯一标示
    private String labelId;
    //时间戳
    private String labelTime;
    //方法名
    private String method;
    //响应体
    private Object responseData;


    public ResponseLog(String labelId, String labelTime, String method, Object responseData) {
        this.labelId = labelId;
        this.labelTime = labelTime;
        this.method = method;
        this.responseData = responseData;
    }
}
