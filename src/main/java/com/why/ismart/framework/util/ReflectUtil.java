package com.why.ismart.framework.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 反射工具类
 * <ul>
 * <li>IoC功能中的类加载后的初始化默认构造函数newInstance、依赖注入设值setField的使用</li>
 * <li>MVC功能的分发处理反射调用方法invokeMethod的使用</li>
 * </ul>
 * 
 * @author whg
 * @date 2016年6月18日 下午4:19:30
 */
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
