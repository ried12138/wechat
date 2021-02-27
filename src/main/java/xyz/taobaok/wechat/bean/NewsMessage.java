package xyz.taobaok.wechat.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;

import java.util.Map;

/**
 *
 * 图文信息
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2021/2/27   3:18 下午
 * @Version 1.0
 */


@XStreamAlias("xml")
public class NewsMessage extends BaseMessage {


    //标题
    @XStreamAlias("Title")
    private String title;
    //描述
    @XStreamAlias("Description")
    private String description;
    //图片
    @XStreamAlias("PicUrl")
    private String picurl;
    //链接
    @XStreamAlias("Url")
    private String url;


    public NewsMessage(Map<String, String> requestMap, String title, String description, String picurl, String url) {
        super(requestMap);
        this.setMsgType("news");
        this.title = title;
        this.description = description;
        this.picurl = picurl;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicurl() {
        return picurl;
    }

    public void setPicurl(String picurl) {
        this.picurl = picurl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
