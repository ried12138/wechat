package xyz.taobaok.wechat.bean.dataoke;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 关键字搜索请求体
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2022/2/14   11:49
 * @Version 1.0
 */
@ApiModel("关键字搜索请求体")
@Data
public class SearchWord {

    @ApiModelProperty("关键词搜索")
    private String wordName;
    @ApiModelProperty("请求的页码，默认参数1")
    private Integer pageId;
    @ApiModelProperty("每页条数，默认为20，最大值100")
    private Integer pageSize;
    @ApiModelProperty("排序字段信息 销量（total_sales） 价格（price），排序_des（降序），排序_asc（升序），示例：升序查询销量total_sales_asc")
    private String sort;
}
