package xin.pwdkeeper.wechat.toolutil;

/**
 * Redis中的固定key值常量类
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2025/3/11   18:56
 * @Version 1.0
 */
public class RedisKeysUtil {
    // 验证码的键前缀
    public static final String VERIFY_CODE_KEY = "verifyCode_";

    // 所有字典类型的键
    public static final String ALL_DICT_TYPES = "allDictTypes";

    // 所有字典项的键
    public static final String ALL_DICT_ITEMS = "allDictItems";
    //请求方平台信息
    public static final String PLATFORM_INFO = "platform_info_";

    public static final String VERIFY_CODE = "verifyCode";


    // 其他固定key值可以继续添加
    // public static final String ANOTHER_KEY = "anotherKey";

    // 私有构造函数，防止实例化
    private RedisKeysUtil() {
        throw new UnsupportedOperationException("Utility class");
    }
}
