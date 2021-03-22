//package xyz.taobaok.wechat.config;
//
//import com.alibaba.fastjson.JSONObject;
//import io.micrometer.core.instrument.Counter;
//import io.micrometer.core.instrument.Gauge;
//import io.micrometer.core.instrument.Timer;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.*;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//import xyz.taobaok.wechat.bean.RequestLog;
//import xyz.taobaok.wechat.bean.ResponseLog;
//import xyz.taobaok.wechat.toolutil.SignMD5Util;
//import xyz.taobaok.wechat.toolutil.WechatMessageUtil;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.Map;
//import java.util.UUID;
//
///**
// * 请求和响应的日志落地
// * 提供给大数据分析的数据
// * @Author weiranliu
// * @Email liuweiran12138@outlook.com
// * @Date 2020/6/29   10:52 上午
// * @Version 1.0
// */
//@Component
//@Aspect
//public class AopLoggerAspect {
//
//    private Logger requestlog = LoggerFactory.getLogger("requestinfo");
//    private Logger responselog = LoggerFactory.getLogger("responseinfo");
////    @Autowired
////    MeterRegistry registry;
//
//    private Counter counter_total;
//    private Gauge gauge_total;
//    private Timer timer;
//    /**
//     * 匹配机制
//     */
//    @Pointcut("execution(* xyz.taobaok.wechat.controller.WeChatController.RequestPostweChat(..))")
//    public void pointCut() {
//    }
//
//    /**
//     *  目标执行前调用
//     * @param joinPoint
//     */
//    @Before("pointCut()")
//    public void boBefore(JoinPoint joinPoint){
//
////        Timer fangfaming = registry.timer("fangfaming");
////        fangfaming.totalTime(TimeUnit.SECONDS);
////        fangfaming.count();
////        fangfaming.max(TimeUnit.SECONDS);
////        Counter total_count_test = registry.counter("total_count_test");
//    }
//
//    /**
//     * 目标执行后调用
//     * @param joinPoint
//     */
//    @After("pointCut()")
//    public void doAfter(JoinPoint joinPoint){
//
//    }
//
//    /**
//     * 目标执行后调用，可以拿到返回值 执行顺序在@After之后
//     * @param joinPoint
//     */
//    @AfterReturning(pointcut = "pointCut()",returning = "result")
//    public void afterReturn(JoinPoint joinPoint, Object result){
//
////        timer.record(()->{
////            try {
////                TimeUnit.SECONDS.sleep(2);
////            } catch (InterruptedException e) {
////                e.printStackTrace();
////            }
////        });
//    }
//
//    /**
//     * 目标执行异常时调用
//     * @param joinPoint
//     * @param throwable
//     */
//    @AfterThrowing(pointcut = "pointCut()",throwing = "throwable")
//    public void afterThrowing(JoinPoint joinPoint, Throwable throwable){
//
//    }
//
//
//    /**
//     * 方法执行前执行后记录
//     * 记录请求参数和响应参数
//     * @param joinPoint
//     * @return
//     */
//    @Around("pointCut()")
//    public Object around(ProceedingJoinPoint joinPoint){
//        Object[] args = joinPoint.getArgs();
//        long nowTime = System.currentTimeMillis();
//        String methodName = joinPoint.getSignature().getName();
//        Object[] args1 = joinPoint.getArgs();
//        String uuid = UUID.randomUUID().toString();
//        RequestLog requestLog = new RequestLog(uuid,SignMD5Util.getStringDate(),methodName, args[0]);
//        requestlog.info(JSONObject.toJSONString(requestLog));
//        try {
//            Object result = joinPoint.proceed();
//            ResponseLog responseLog = new ResponseLog(uuid,SignMD5Util.getStringDate(),methodName,result);
//            responselog.info(JSONObject.toJSONString(responseLog));
////            long end = System.currentTimeMillis();
//            return result;
//        } catch (Throwable throwable) {
//            throw new RuntimeException(throwable.getMessage());
//        }
//    }
//}
