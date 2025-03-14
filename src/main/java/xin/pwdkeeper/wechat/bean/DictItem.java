package xin.pwdkeeper.wechat.bean;

import lombok.Data;

/**
 * 字典项实体类
 */
@Data
public class DictItem {
    // 主键，唯一标识每个字典项
    private Integer itemId;
    // 外键，关联到 dict_type 表的 type_id
    private Integer typeId;
    //字典等级，1就等于是1级字典，2属于二级字典
    private Integer grade;
    //是否有二级字典，如果有这里就指向二级字典的item_id
    private Integer gradeTypeId;
    // 字典项的代码，通常是唯一的标识符
    private String itemCode;
    // 字典项的值或名称
    private String itemValue;
    // 对字典项的描述
    private String description;
    // 排序字段，用于定义字典项在列表中的顺序
    private Integer sortOrder;
    //是否启用 0=不可用 1=可用
    private Integer flag;

}
