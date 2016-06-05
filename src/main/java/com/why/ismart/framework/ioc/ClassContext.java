package com.why.ismart.framework.ioc;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.why.ismart.framework.config.Config;
import com.why.ismart.framework.mvc.Controller;
import com.why.ismart.framework.util.ClassUtil;

public class ClassContext {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClassContext.class);
    
    private static final Set<Class<?>> CLASS_SET;
    static{
        LOGGER.info("ClassContext static init...");
        String basePackage = Config.getAppBasePackage();
        CLASS_SET = ClassUtil.loadClassSet(basePackage);
    }
    
    public static Set<Class<?>> getServices(){
        return getClassesByAnnotation(Service.class);
    }
    
    public static Set<Class<?>> getControllers(){
        return getClassesByAnnotation(Controller.class);
    }
    
    public static Set<Class<?>> getClassesByAnnotation(Class<? extends Annotation> annotation){
        Set<Class<?>> classes = new HashSet<Class<?>>();
        for(Class<?> clazz:CLASS_SET){
            if(clazz.isAnnotationPresent(annotation)){
                classes.add(clazz);
            }
        }
        return classes;
    }
    
    public static Set<Class<?>> getBeans(){
        Set<Class<?>> classSetes = new HashSet<Class<?>>();
        classSetes.addAll(getServices());
        classSetes.addAll(getControllers());
        return classSetes;
    }
    
    public static Set<Class<?>> getSubClasses(Class<?> superClass){
        Set<Class<?>> classes = new HashSet<Class<?>>();
        for(Class<?> clazz:CLASS_SET){
            if(superClass.isAssignableFrom(clazz) && !superClass.equals(clazz)){
                classes.add(clazz);
            }
        }
        return classes;
    }
    
}
