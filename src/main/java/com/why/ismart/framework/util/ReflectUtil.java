package com.why.ismart.framework.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReflectUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReflectUtil.class);
    
    public static Object newInstance(Class<?> clazz){
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("newInstance error", e);
            throw new RuntimeException(e);
        }
    }
    
    public static Object invokeMethod(Object obj, Method method, Object... args){
        method.setAccessible(true);
        try {
            return method.invoke(obj, args);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("invokeMethod error", e);
            throw new RuntimeException(e);
        }
    }
    
    public static void setField(Object obj, Field field, Object value){
        field.setAccessible(true);
        try {
            field.set(obj, value);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("setField error", e);
            throw new RuntimeException(e);
        }
    }
    
}
