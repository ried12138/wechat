package xin.pwdkeeper.wechat.config;

import com.alibaba.fastjson.JSONObject;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xin.pwdkeeper.wechat.bean.RequestLog;
import xin.pwdkeeper.wechat.bean.RequestParams;
import xin.pwdkeeper.wechat.bean.ResponseLog;
import xin.pwdkeeper.wechat.service.RedisService;
import xin.pwdkeeper.wechat.toolutil.AesUtil;
import xin.pwdkeeper.wechat.toolutil.DateTimeUtil;
import xin.pwdkeeper.wechat.toolutil.RedisKeysUtil;
import xin.pwdkeeper.wechat.toolutil.SignMD5Util;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * 拦截数据请求
 * 请求和响应的日志落地
 * 提供给大数据分析的数据
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2020/6/29   10:52 上午
 * @Version 1.0
 */
@Component
@Aspect
public class AopVolleyAspect {

    private final String PLATFROM_UNIQUE = "platfrom_unique";
    private Logger requestlog = LoggerFactory.getLogger("requestinfo");
    private Logger responselog = LoggerFactory.getLogger("responseinfo");

    @Autowired
    private RedisService redisService;
    /**
     * 匹配机制
     */
    @Pointcut("execution(* xin.pwdkeeper.wechat.controller.WeChatController.handleMessage(..))")
    public void wechatHandleMessage() {}

    @Pointcut("execution(* xin.pwdkeeper.wechat.controller.WebFrontController.*(..)) && !execution(* xin.pwdkeeper.wechat.controller.WebFrontController.generateVerifyCode(..))")
    public void webFrontAll() {}

    @Pointcut("execution(* xin.pwdkeeper.wechat.controller.WebFrontController.generateVerifyCode(..))")
    public void webFrontByGenerateVerifyCode() {}
    /**
     *  目标执行前调用
     * @param joinPoint
     */
    @Before("webFrontAll()")
    public void boBefore(JoinPoint joinPoint) throws NoSuchPaddingException, IllegalBlockSizeException, UnsupportedEncodingException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        Object[] args = joinPoint.getArgs();
        if (args != null && args.length > 0 && args[0] instanceof RequestParams) {
            RequestParams requestParams = (RequestParams) args[0];
            // 进行参数校验
            if (requestParams.getRequestPlatFrom() == null){
                throw new IllegalArgumentException("请求方所属平台不能为空");
            }
            if (requestParams.getRequestId() == null){
                throw new IllegalArgumentException("请求方唯一标识不能为空");
            }
            if (requestParams.getUserId() == null){
                throw new IllegalArgumentException("用户唯一标识不能为空");
            }
            if (requestParams.getTimestamp() == null){
                throw new IllegalArgumentException("请求时间戳不能为空");
            }
            if (!DateTimeUtil.isWithinOneMinute(requestParams.getTimestamp(),1)){
                throw new IllegalArgumentException("请求体超时");
            }
            // 添加方法参数注入
            String platfromRequestId = redisService.get(RedisKeysUtil.PLATFORM_INFO + requestParams.getRequestPlatFrom(),String.class);
            if (platfromRequestId == null) {
                throw new IllegalArgumentException("请求方所属平台错误");
            }
            if (!platfromRequestId.equals(requestParams.getRequestId())){
                throw new IllegalArgumentException("请求方唯一标识错误");
            }
            //解密userId
            String openId = (String) redisService.get(requestParams.getUserId());
            if (openId != null){
                String decrypt = AesUtil.decrypt(requestParams.getUserId());
                String time = decrypt.split("/")[1];
                if (time != null){
                    if (!DateTimeUtil.isWithinOneMinute(Long.valueOf(time),25)){
                        throw new IllegalArgumentException("过期的地址，请重新申请");
                    }
                }
                requestParams.setUserId(openId);
            }else{
                throw new IllegalArgumentException("过期的地址，请重新申请");
            }
        } else {
            throw new IllegalArgumentException("请求参数格式错误");
        }
    }

        /**
         * 目标执行后调用
         * @param joinPoint
         */
//    @Around("webFrontAll()")
//    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {};

    /**
     * 目标执行后调用，可以拿到返回值 执行顺序在@After之后
     * @param joinPoint
     */
//    @AfterReturning(pointcut = "pointCut()",returning = "result")
//    public void afterReturn(JoinPoint joinPoint, Object result){
//        timer.record(()->{
//            try {
//                TimeUnit.SECONDS.sleep(2);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        });
//    }

    /**
     * 目标执行异常时调用
     * @param joinPoint
     * @param throwable
     */
//    @AfterThrowing(pointcut = "pointCut()",throwing = "throwable")
//    public void afterThrowing(JoinPoint joinPoint, Throwable throwable){
//
//    }


    /**
     * 方法执行前执行后记录
     * 记录请求参数和响应参数
     * @param joinPoint
     * @return
     */
    @Around("wechatHandleMessage()")
    public Object around(ProceedingJoinPoint joinPoint){
        Object[] args = joinPoint.getArgs();
        long nowTime = System.currentTimeMillis();
        String methodName = joinPoint.getSignature().getName();
        Object[] args1 = joinPoint.getArgs();
        String uuid = UUID.randomUUID().toString();
        RequestLog requestLog = new RequestLog(uuid,SignMD5Util.getStringDate(),methodName, args[0]);
        requestlog.info(JSONObject.toJSONString(requestLog));
        try {
            Object result = joinPoint.proceed();
            ResponseLog responseLog = new ResponseLog(uuid,SignMD5Util.getStringDate(),methodName,result);
            responselog.info(JSONObject.toJSONString(responseLog));
            long end = System.currentTimeMillis();
            return result;
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable.getMessage());
        }
    }
}
