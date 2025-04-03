package xin.pwdkeeper.wechat.bean;

import lombok.Data;

import java.util.Date;

@Data
public class FileDataInfo {
    // 主键
    private Integer id;
    // 绑定的openId
    private Integer openId;
    //账号id
    private Integer accId;
    // 文件名称
    private String fileName;
    // 文件url
    private String fileUrl;
    // 文件所在桶(minIO)
    private String fileBucketName;
    // 创建时间
    private Date creationTime;
    // 更新时间
    private Date updateTime;
    // 是否展示0=不展示 1=展示
    private Integer flag;

    public FileDataInfo() {
    }

    public FileDataInfo(Integer id, Integer openId,Integer accId, String fileName, String fileUrl, String fileBucketName, Date creationTime, Date updateTime, Integer flag) {
        if (id != null) this.id = id;
        if (openId != null) this.openId = openId;
        if (accId != null) this.accId = accId;
        if (fileName != null) this.fileName = fileName;
        if (fileUrl != null) this.fileUrl = fileUrl;
        if (fileBucketName != null) this.fileBucketName = fileBucketName;
        if (creationTime != null){
            this.creationTime = creationTime;
        }else{
            this.creationTime = new Date();
        }
        if (updateTime != null){
            this.updateTime = updateTime;
        }else{
            this.updateTime = new Date();
        }
        if (flag != null){
            this.flag = flag;
        }else{
            this.flag = 0;
        }
    }
}