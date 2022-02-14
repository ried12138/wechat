package xyz.taobaok.wechat.bean.dataoke;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2022/2/14   14:57
 * @Version 1.0
 */
@ApiModel("9块9包邮")
@Data
public class CheapSelect {
    @ApiModelProperty("每页条数：默认为20，最大值100")
    private Integer pageSize;
    @ApiModelProperty("分页id：常规分页方式，请直接传入对应页码（比如：1,2,3……）")
    private String pageId;
    @ApiModelProperty("9.9精选的类目id，分类id请求详情：-1 =精选，1 =5.9元区，2 =9.9元区，3 =19.9元区（调整字段）")
    private String nineCid;
}
