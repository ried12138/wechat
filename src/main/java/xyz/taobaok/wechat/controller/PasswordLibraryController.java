package xyz.taobaok.wechat.controller;

import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.taobaok.wechat.service.PasswordLibraryService;

/**
 *
 *   校验软件获取验证码
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2022/1/28   10:10
 * @Version 1.0
 */

@Slf4j
@RestController
@RequestMapping("/")
public class PasswordLibraryController {


    @Autowired
    PasswordLibraryService passwordLibraryService;

    @RequestMapping(value = "/checkCode",method = RequestMethod.GET,produces = "text/html;charset=utf-8")
    @ResponseBody
    public String checkCode(){
        return passwordLibraryService.checkCodeReturn();
    }
}
