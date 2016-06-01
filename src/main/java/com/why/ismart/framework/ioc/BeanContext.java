package com.why.ismart.framework.ioc;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.why.ismart.framework.util.ReflectUtil;

public class BeanContext {

    private static final Map<Class<?>, Object> BEAN_MAP;
    static{
        BEAN_MAP = new HashMap<Class<?>, Object>();
        Set<Class<?>> beans = ClassContext.getBeans();
        for(Class<?> clazz:beans){
            BEAN_MAP.put(clazz, ReflectUtil.newInstance(clazz));
        }
    }
    
    public static Map<Class<?>, Object> getBeanMap(){
        return BEAN_MAP;
    }
    
    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<T> clazz){
        if(!BEAN_MAP.containsKey(clazz)){
            throw new RuntimeException("Bean class not found:"+clazz);
        }
        return (T)BEAN_MAP.get(clazz);
    }
    
}
