package xyz.taobaok.wechat;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
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
        if ("org.apache.catalina.connector.ClientAbortException".equals(ex.getClass().getName())) {
            log.error("发生clientAbortException");
            return null;
        }
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Type", "application/json;charset=UTF-8");
        return new ResponseEntity(
                "{\"code\":500,\"data\":null,\"msg\":\"服务器闹脾气，请稍后再试\"}", httpHeaders, HttpStatus.OK);
    }
}
