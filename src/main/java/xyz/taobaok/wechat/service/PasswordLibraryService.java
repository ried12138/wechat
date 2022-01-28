package xyz.taobaok.wechat.service;

/**
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2022/1/28   10:21
 * @Version 1.0
 */
public interface PasswordLibraryService {


    String checkCodeReturn();

    String codeLanguage(String code,String fromUserName);
}
