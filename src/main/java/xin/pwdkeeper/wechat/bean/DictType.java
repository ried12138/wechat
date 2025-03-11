package xin.pwdkeeper.wechat.bean;

import lombok.Data;

/**
 * 字典类型实体类
 */
@Data
public class DictType {
    // 主键，唯一标识每种字典类型
    private Integer typeId;
    // 字典类型的名称
    private String typeName;
    // 对字典类型的描述
    private String description;
    // 是否启用 0=不启用，1=启用
    private Integer flag;

}
