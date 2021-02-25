package xyz.taobaok.wechat.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @Author weiranliu
 * @Email liuweiran12138@outlook.com
 * @Date 2021/2/24   7:33 下午
 * @Version 1.0
 */
@Component
public class BeanManager implements ApplicationContextAware {
    private volatile static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext context)
            throws BeansException {
        BeanManager.context = context;
    }
    public static ApplicationContext getContext(){
        return context;
    }

    public static Object getBean(String beanName){
        return context.getBean(beanName);
    }

    public static <T> T getBean(Class<T> resourceUrlProviderClass) {
        return context.getBean(resourceUrlProviderClass);
    }
}
