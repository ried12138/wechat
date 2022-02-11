package xyz.taobaok.wechat.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 首页活动banner响应体
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2022/2/11   15:41
 * @Version 1.0
 */
@ApiModel("首页活动banner")
@Data
public class ActivityBanner {
    @ApiModelProperty("活动id")
    private Integer id;					//活动id	Number	1
    @ApiModelProperty("活动名称")
    private String activityName;			//活动名称	String	“聚划算-日常-品牌清仓”
    @ApiModelProperty("活动开始时间")
    private String activityStartTime;	//活动开始时间	String	“2020-03-02 00:00:00”
    @ApiModelProperty("活动结束时间")
    private String activityEndTime;		//活动结束时间	String	“2020-03-31 23:59:59”
    @ApiModelProperty("活动信息")
    private String activityInfo;			//活动信息	String	清仓info
    @ApiModelProperty("端口类型")
    private Integer type;				//端口类型	Number	1
    @ApiModelProperty("活动链接")
    private String activityLink;		//活动链接	String	https://
    @ApiModelProperty("素材链接")
    private String materialLink;		//素材链接	String	“https://gw.alicdn.com/tfs/TB1iUMatQP2gK0jSZPxXXacQpXa-440-180.jpg“
}
