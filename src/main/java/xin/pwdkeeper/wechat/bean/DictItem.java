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
    // 字典项的代码，通常是唯一的标识符
    private String itemCode;
    // 字典项的值或名称
    private String itemValue;
    // 对字典项的描述
    private String description;
    // 排序字段，用于定义字典项在列表中的顺序
    private Integer sortOrder;

}
