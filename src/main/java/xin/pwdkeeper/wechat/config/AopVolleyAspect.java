package xin.pwdkeeper.wechat.config;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import xin.pwdkeeper.wechat.bean.AccountInfo;
import xin.pwdkeeper.wechat.bean.RequestLog;
import xin.pwdkeeper.wechat.bean.RequestParams;
import xin.pwdkeeper.wechat.bean.ResponseLog;
import xin.pwdkeeper.wechat.customizeService.RedisService;
import xin.pwdkeeper.wechat.util.AesUtil;
import xin.pwdkeeper.wechat.util.DateTimeUtil;
import xin.pwdkeeper.wechat.util.RedisKeysUtil;
import xin.pwdkeeper.wechat.util.SignMD5Util;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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

    @Value("${url.aging.timeliness}")
    private Integer minute;
    @Autowired
    private RedisService redisService;
    /**
     * 微信公众号方向请求的拦截
     */
    @Pointcut("execution(* xin.pwdkeeper.wechat.controller.WeChatController.handleMessage(..))")
    public void wechatHandleMessage() {}

    /**
     * 拦截web请求添加用户信息
     */
    @Pointcut("execution(* xin.pwdkeeper.wechat.controller.WebFrontController.addUserInfoData(..))")
    public void addUserInfoData() {}

    /**
     * 拦截web请求批量移除用户信息、资产数据
     */
    @Pointcut("execution(* xin.pwdkeeper.wechat.controller.WebFrontController.removeUserInfoData(..))")
    public void removeUserInfoData() {}

    /**
     * 拦截web请求修改用户信息
     */
    @Pointcut("execution(* xin.pwdkeeper.wechat.controller.WebFrontController.webAlterUserInfoData(..))")
    public void webAlterUserInfoData() {}
    @Pointcut("execution(* xin.pwdkeeper.wechat.controller.WebFrontController.webFetchUserInfoData(..))")
    public void webFetchUserInfoData() {}
    /**
     * 拦截校验验证码的接口
     */
    @Pointcut("execution(* xin.pwdkeeper.wechat.controller.VerifyCodeController.getVerifyCode(..))")
    public void verificationCode() {}

    /**
     * 拦截生成验证码接口
     */
    @Pointcut("execution(* xin.pwdkeeper.wechat.controller.VerifyCodeController.*(..))")
    public void webFrontByGenerateVerifyCode() {}

    /**
     * 基础参数校验方法抽取
     * @param joinPoint
     * @param excludeField 排除需要校验的字段 例如openId
     * @return
     */
    private RequestParams verifyBasicParameters(JoinPoint joinPoint, List<String> excludeField){
        Object[] args = joinPoint.getArgs();
        if (args != null && args.length > 0 && args[0] instanceof RequestParams) {
            RequestParams requestParams = (RequestParams) args[0];
            // 进行参数校验
            if (requestParams.getRequestPlatFrom() == null) {
                throw new IllegalArgumentException("请求方所属平台不能为空");
            }
            if (requestParams.getRequestId() == null) {
                throw new IllegalArgumentException("请求方唯一标识不能为空");
            }
            if (requestParams.getOpenId() == null) {
                throw new IllegalArgumentException("用户唯一标识不能为空");
            }
            if (requestParams.getTimestamp() == null) {
                throw new IllegalArgumentException("请求时间戳不能为空");
            }
            if (!DateTimeUtil.isWithinOneMinute(requestParams.getTimestamp(), 1)) {
                throw new IllegalArgumentException("请求体超时");
            }
            // 添加方法参数注入
            String platfromRequestId = redisService.get(RedisKeysUtil.PLATFORM_INFO + requestParams.getRequestPlatFrom(), String.class);
            if (platfromRequestId == null) {
                throw new IllegalArgumentException("请求方所属平台错误");
            }
            if (!platfromRequestId.equals(requestParams.getRequestId())) {
                throw new IllegalArgumentException("请求方唯一标识错误");
            }
            if (!excludeField.contains("openId")){
                verifyBasicOpenId(requestParams);
            }
            return requestParams;
        }else {
            throw new IllegalArgumentException("请求体格式不对");
        }
    }

    /**
     * 校验openId
     * @param requestParams
     * @return
     */
    private RequestParams verifyBasicOpenId(RequestParams requestParams){
        //解密userId
        String openId = (String) redisService.get(requestParams.getOpenId());
        if (openId != null){
            String decrypt = null;
            try {
                decrypt = AesUtil.decrypt(requestParams.getOpenId());
            } catch (Exception e) {
                throw new IllegalArgumentException("解码openId出现了致命的错误，请确保openId是从官方获取");
            }
            String time = decrypt.split("/")[1];
            if (time != null){
                /**
                 * 这里对时间校验的逻辑是保护服务生产的key在被服务器本自己消费掉，
                 * 目前redis 存放这个key的时间是根据minute时长，生产时间和失效时间是同一个minute时长
                 */
                if (!DateTimeUtil.isWithinOneMinute(Long.valueOf(time),minute)){
                    throw new IllegalArgumentException("过期的地址，请重新申请");
                }
            }
            requestParams.setOpenId(openId);
        }else{
            throw new IllegalArgumentException("授权失效,请重新获取权限");
        }
        return requestParams;
    }

    /**
     * 校验验证码请求拦截
     * @param joinPoint
     */
    @Before("verificationCode()")
    public void verificationCode(JoinPoint joinPoint) {
        verifyBasicParameters(joinPoint, Arrays.asList("full"));
    }

    /**
     * 添加资产接口请求
     * @param joinPoint
     */
    @Before("addUserInfoData()")
    public void addUserInfoData(JoinPoint joinPoint) {
        RequestParams requestParams = verifyBasicParameters(joinPoint,Arrays.asList("full"));
        //校验body体
        Object requestBody = requestParams.getRequestBody();
        if (requestBody != null) {
            ObjectMapper mapper = new ObjectMapper();
            AccountInfo accountInfo = mapper.convertValue(requestBody, AccountInfo.class);
            if(accountInfo.isAccountEmpty()){
                throw new IllegalArgumentException("账号不能为空");
            }
            if(accountInfo.isBindPhoneEmpty()){
                throw new IllegalArgumentException("手机号格式不对");
            }
            if(accountInfo.isBindEmailEmpty()){
                throw new IllegalArgumentException("邮箱格式不对");
            }
            requestParams.setRequestBody(accountInfo);
        }else{
            throw new IllegalArgumentException("请求体格式不对");
        }
    }


    /**
     * 移除资产请求接口
     * @param joinPoint
     */
    @Before("removeUserInfoData()")
    public void removeUserInfoData(JoinPoint joinPoint){
        RequestParams requestParams = verifyBasicParameters(joinPoint,Arrays.asList("full"));
        Object requestBody = requestParams.getRequestBody();
        if (requestBody != null && requestBody instanceof List<?>){
            List<Integer> ids = (List<Integer>) requestBody;
            if (ids != null && ids.size() > 0){
                requestParams.setRequestBody(ids);
            }else{
                throw new IllegalArgumentException("请求参数不能为空");
            }
        }else{
            throw new IllegalArgumentException("请求体格式不对");
        }
    }

    /**
     * 单体修改资产数据接口
     * @param joinPoint
     */
    @Before("webAlterUserInfoData()")
    public void webAlterUserInfoData(JoinPoint joinPoint) {
        RequestParams requestParams = verifyBasicParameters(joinPoint, Arrays.asList("full"));
        Object requestBody = requestParams.getRequestBody();
        if (requestBody != null && requestBody instanceof List<?>){
            List<AccountInfo> accountInfos = (List<AccountInfo>) requestBody;
            if (accountInfos != null && accountInfos.size() > 0){
                requestParams.setRequestBody(accountInfos);
            }else{
                throw new IllegalArgumentException("请求参数不能为空");
            }
        }else{
            throw new IllegalArgumentException("请求体格式不对");
        }
    }
    @Before("webFetchUserInfoData()")
    public void webFetchUserInfoData(JoinPoint joinPoint) {
        RequestParams requestParams = verifyBasicParameters(joinPoint, Arrays.asList("full"));
        Object requestBody = requestParams.getRequestBody();
        if (requestBody != null){
            Map<String, Object> data = (Map<String, Object>)requestBody;
            if (data.get("pageNum") == null || data.get("pageSize") == null){
                throw new IllegalArgumentException("请求参数不能为空,请根据API接口文档传参");
            }
        }else{
            throw new IllegalArgumentException("请求体格式不对");
        }
    }
    /**
     * 生产验证码接口进行拦截校验入参
     * @param joinPoint
     */
    @Before("webFrontByGenerateVerifyCode()")
    public void GenerateVerifyCode(JoinPoint joinPoint){
        verifyBasicParameters(joinPoint,Arrays.asList("openId"));
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
