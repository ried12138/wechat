package xin.pwdkeeper.wechat.bean;

import com.fasterxml.jackson.databind.ObjectMapper;
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

    public DictItem() {
    }

    public DictItem(Integer typeId, Integer grade, Integer gradeTypeId, String itemCode, String itemValue, String description, Integer sortOrder) {
        this.typeId = typeId;
        this.grade = grade;
        this.gradeTypeId = gradeTypeId;
        this.itemCode = itemCode;
        this.itemValue = itemValue;
        this.description = description;
        this.sortOrder = sortOrder;
    }

    public String acquireAiByAnalyze() {
        if (itemValue != null && itemValue.length() > 0) {
            String ai = itemValue + "，截取网站域名中间部分，用20个字以内描述这个平台是干什么的，返回给我一个json格式的数据，itemValue: "+itemValue+",itemCode:网站域名中间部分,description：简单描述";
            return ai;
        }
        return null;
    }

    /**
     * 封装要保存的字典
     * @param json
     * @return
     */
    public DictItem analyseAIresults(String json) {
        try {
            // 去掉开头和结尾的引号
            if (json.startsWith("\"") && json.endsWith("\"")){
                json = json.substring(1, json.length() - 2);
            }
            // 去掉 ```json\n 和 \n```
            json = json.replace("```json\n", "");
            json = json.replace("\n```", "");
            ObjectMapper objectMapper = new ObjectMapper();
            DictItem parsedItem = objectMapper.readValue(json, DictItem.class);
            this.itemValue = parsedItem.getItemValue();
            this.itemCode = parsedItem.getItemCode();
            this.description = parsedItem.getDescription();
            this.sortOrder = sortOrder+1;
            return parsedItem;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}