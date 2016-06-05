package com.why.ismart.framework.ioc;

import java.lang.reflect.Field;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.why.ismart.framework.aop.AopContext;
import com.why.ismart.framework.util.ReflectUtil;

public class IocContext {

    private static final Logger LOGGER = LoggerFactory.getLogger(IocContext.class);
    
    static{
        LOGGER.info("IocContext static init...");
        Map<Class<?>, Object> beanMap = BeanContext.getBeanMap();
        for(Map.Entry<Class<?>, Object> entry:beanMap.entrySet()){
            Class<?> clazz = entry.getKey();
            Object instance = entry.getValue();
            Field[] fields = clazz.getDeclaredFields();
            for(Field field:fields){
                if(field.isAnnotationPresent(Inject.class)){
                    LOGGER.info("Inject "+clazz.getName()+" "+field.getName());
                    ReflectUtil.setField(instance, field, BeanContext.getBean(field.getType()));
                }
            }
        }
    }
    
}
