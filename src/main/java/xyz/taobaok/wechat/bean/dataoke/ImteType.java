package xyz.taobaok.wechat.bean.dataoke;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2022/2/14   15:32
 * @Version 1.0
 */
@ApiModel("商品分类")
@Data
public class ImteType {

    @ApiModelProperty("一级分类ID")
    private Integer cid;	                                //一级分类ID，1 -女装，2 -母婴，3 -美妆，4 -居家日用，5 -鞋品，6 -美食，7 -文娱车品，8 -数码家电，9 -男装，10 -内衣，11 -箱包，12 -配饰，13 -户外运动，14 -家装家纺	private String Number	1
    @ApiModelProperty("一级分类名称")
    private String cname;	                                //一级分类名称	String	女装
    @ApiModelProperty("一级分类图标")
    private String cpic;	                                //一级分类图标	string	https://img.alicdn.com/imgextra/i1/2053469401/TB2oX82HL9TBuNjy0FcXXbeiFXa-2053469401.png
    @ApiModelProperty("二级分类")
    private List<ItemTwoType> subcategories;               //二级分类
}
