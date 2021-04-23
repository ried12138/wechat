package xyz.taobaok.wechat.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.Map;

/**
 * 图片信息处理相关属性
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2021/4/23   3:47 下午
 * @Version 1.0
 */
@XStreamAlias("xml")
public class ImageMessage extends BaseMessage {


    @XStreamAlias("MediaId")
    private String mediaId; //图片消息媒体id

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public ImageMessage(Map<String, String> requestMap,String mediaId) {
        super(requestMap);
        this.setMsgType("image");
        this.mediaId = mediaId;
    }
}
