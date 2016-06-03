package com.why.ismart.framework.ioc;

import java.util.HashSet;
import java.util.Set;

import com.why.ismart.framework.config.Config;
import com.why.ismart.framework.mvc.Controller;
import com.why.ismart.framework.util.ClassUtil;

public class ClassContext {

    private static final Set<Class<?>> CLASS_SET;
    static{
        String basePackage = Config.getAppBasePackage();
        CLASS_SET = ClassUtil.loadClassSet(basePackage);
    }
    
    public static Set<Class<?>> getServices(){
        Set<Class<?>> classSet = new HashSet<Class<?>>();
        for(Class<?> clazz:CLASS_SET){
            if(clazz.isAnnotationPresent(Service.class)){
                classSet.add(clazz);
            }
        }
        return classSet;
    }
    
    public static Set<Class<?>> getControllers(){
        Set<Class<?>> classSet = new HashSet<Class<?>>();
        for(Class<?> clazz:CLASS_SET){
            if(clazz.isAnnotationPresent(Controller.class)){
                classSet.add(clazz);
            }
        }
        return classSet;
    }
    
    public static Set<Class<?>> getBeans(){
        Set<Class<?>> classSet = new HashSet<Class<?>>();
        classSet.addAll(getServices());
        classSet.addAll(getControllers());
        return classSet;
    }
    
}
