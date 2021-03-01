package xyz.taobaok.wechat.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2021/3/1   7:30 下午
 * @Version 1.0
 */

public class Articles {
    @XStreamAlias("item")
    public Item item;


    public Articles(Item item) {
        this.item = item;
    }
}
