package xin.pwdkeeper.wechat.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * 该方法用于全局捕获异常并返回统一的错误响应。具体功能如下：
 * 1、捕获所有类型的异常并记录详细的错误信息。
 * 2、如果是ClientAbortException，额外记录请求信息。
 * 3、设置响应头为JSON格式，返回500错误码和友好的错误消息。
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2021/4/14   2:32 下午
 * @Version 1.0
 */
@ControllerAdvice
@Slf4j
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(Exception.class)
    protected ResponseEntity handleExceptions(Exception ex, WebRequest request) {
        // 记录详细的错误信息
        log.error("发生异常：\n{}", ex.getMessage(), ex);
        HttpHeaders httpHeaders = new HttpHeaders();
        //如果是IllegalArgumentException错误就判断为AOP拦截入参错误
        if (ex instanceof IllegalArgumentException) {
            // 返回具体的错误信息
            return new ResponseEntity(
                    String.format("{\"code\":1,\"data\":null,\"msg\":\"%s\"}", ex.getMessage()), httpHeaders, HttpStatus.BAD_REQUEST);
        }
        if ("org.apache.catalina.connector.ClientAbortException".equals(ex.getClass().getName())) {
            log.error("发生clientAbortException：\n{}",request.toString());
        }
        httpHeaders.set("Content-Type", "application/json;charset=UTF-8");
        return new ResponseEntity(
                "{\"code\":500,\"data\":null,\"msg\":\"服务器闹脾气，请稍后再试\"}", httpHeaders, HttpStatus.OK);
    }
}
