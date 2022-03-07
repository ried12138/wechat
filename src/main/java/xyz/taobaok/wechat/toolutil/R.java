package xyz.taobaok.wechat.toolutil;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2022/2/11   15:33
 * @Version 1.0
 */
@ApiModel("响应结果封装体")
@Data
public class R<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("返回标记：成功标记=0，失败标记=1")
    private int code;

    @ApiModelProperty("返回信息")
    private String msg;

    @ApiModelProperty("数据")
    private T data;

    public static <T> R<T> ok() {
        return restResult(null, CommonConstants.SUCCESS, null);
    }

    public static <T> R<T> ok(T data) {
        return restResult(data, CommonConstants.SUCCESS, "SUCCESS");
    }

    public static <T> R<T> failed(T data) {
        return restResult(data, CommonConstants.FAIL, "FAILED");
    }

    public static <T> R<T> failed(T data, String msg) {
        return restResult(data, CommonConstants.FAIL, msg);
    }

    private static <T> R<T> restResult(T data, int code, String msg) {
        R<T> apiResult = new R<>();
        apiResult.setCode(code);
        apiResult.setData(data);
        apiResult.setMsg(msg);
        return apiResult;
    }
}
