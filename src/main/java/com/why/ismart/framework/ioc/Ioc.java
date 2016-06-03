package com.why.ismart.framework.ioc;

import java.lang.reflect.Field;
import java.util.Map;

import com.why.ismart.framework.util.ReflectUtil;

public class Ioc {

    static{
        Map<Class<?>, Object> beanMap = BeanContext.getBeanMap();
        for(Map.Entry<Class<?>, Object> entry:beanMap.entrySet()){
            Class<?> clazz = entry.getKey();
            Object instance = entry.getValue();
            Field[] fields = clazz.getDeclaredFields();
            for(Field field:fields){
                if(field.isAnnotationPresent(Inject.class)){
                    System.out.println("Inject "+clazz.getName()+" "+field.getName());
                    ReflectUtil.setField(instance, field, BeanContext.getBean(field.getType()));
                }
            }
        }
    }
    
}
