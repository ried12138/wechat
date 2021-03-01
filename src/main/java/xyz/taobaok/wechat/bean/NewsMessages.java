package xyz.taobaok.wechat.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.Map;

/**
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2021/3/1   7:25 下午
 * @Version 1.0
 */
@XStreamAlias("xml")
public class NewsMessages extends BaseMessage{

    @XStreamAlias("Articles")
    public Articles articles;
    @XStreamAlias("ArticleCount")
    public int articleCount;

    public NewsMessages(Map<String, String> requestMap,String title, String description, String picurl, String url) {
        super(requestMap);
        this.setMsgType("news");
        this.articleCount = 1;
        Item item = new Item();
        item.setTitle(title);
        item.setDescription(description);
        item.setPicurl(picurl);
        item.setUrl(url);
        this.articles = new Articles(item);
    }

}
