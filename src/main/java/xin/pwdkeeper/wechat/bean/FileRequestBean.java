package xin.pwdkeeper.wechat.bean;

import lombok.Data;

/**
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2025/3/31   17:22
 * @Version 1.0
 */
@Data
public class FileRequestBean {

    /**
     * 文件对象名称
     */
    private String objectName;
    //图片格式image/png,base64
    private String base64Image;
}
