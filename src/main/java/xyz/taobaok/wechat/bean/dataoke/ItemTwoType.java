package xyz.taobaok.wechat.bean.dataoke;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2022/2/14   15:34
 * @Version 1.0
 */
@ApiModel("二级分类")
@Data
public class ItemTwoType {
    @ApiModelProperty("二级分类Id")
    private Integer subcid;	                        //二级分类Id，根据实际返回id为准	Number	98713
    @ApiModelProperty("二级分类名称")
    private String subcname;	                    //二级分类名称	String	T恤/短袖
    @ApiModelProperty("二级分类图标")
    private String scpic;	                        //二级分类图标	string	https://img.alicdn.com/imgextra/i1/2053469401/TB2oX82HL9TBuNjy0FcXXbeiFXa-2053469401.pngprivate String

}
