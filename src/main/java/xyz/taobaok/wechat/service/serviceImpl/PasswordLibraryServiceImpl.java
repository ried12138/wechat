package xyz.taobaok.wechat.service.serviceImpl;

import org.springframework.stereotype.Service;
import xyz.taobaok.wechat.service.PasswordLibraryService;
import xyz.taobaok.wechat.toolutil.AesUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2022/1/28   10:24
 * @Version 1.0
 */
@Service
public class PasswordLibraryServiceImpl implements PasswordLibraryService {

    private static Map<String,String> map = new HashMap<>();
    /**
     * 生成验证信息，并缓存在本地
     * @return
     */
    @Override
    public String checkCodeReturn() {
        String code = String.valueOf((int) ((Math.random() * 9 + 1) * 100000));
        map.put("key",code);
        return "success";
    }

    /**
     * 返回验证信息
     * @param code
     * @return
     */
    @Override
    public String codeLanguage(String code,String from) {
        String str = "校验码：";
        if (code.equals("唯有光才是真实的") && from.equals("oMvXct8DYZjPLNqXv4FiqQygfCmo")){
            str += map.get("key");
            map.remove("key");
        }
        return str;
    }
}
