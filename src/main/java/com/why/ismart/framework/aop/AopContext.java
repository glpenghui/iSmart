package com.why.ismart.framework.aop;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.why.ismart.framework.ioc.BeanContext;
import com.why.ismart.framework.ioc.ClassContext;

public class AopContext {

    private static final Logger LOGGER = LoggerFactory.getLogger(AopContext.class);
    
    static{
        LOGGER.info("AopContext static init...");
        Map<Class<?>, List<Proxy>> beanClass2ProxyListMap = parseBeanClass2ProxyListMap(getProxy2BeanClassesMap());
        for(Map.Entry<Class<?>, List<Proxy>> entry:beanClass2ProxyListMap.entrySet()){
            Class<?> beanClass = entry.getKey();
            List<Proxy> proxyList = entry.getValue();
            BeanContext.setBean(beanClass, ProxyManager.createProxy(beanClass, proxyList));
        }
    }
    
    private static Map<Class<?>, Set<Class<?>>> getProxy2BeanClassesMap(){
        Map<Class<?>, Set<Class<?>>> result = new HashMap<Class<?>, Set<Class<?>>>();
        result.putAll(getTransactionProxy2BeanClassesMap());
        result.putAll(getAspectProxy2BeanClassesMap());
        return result;
    }
    
    private static Map<Class<?>, Set<Class<?>>> getTransactionProxy2BeanClassesMap() {
        Map<Class<?>, Set<Class<?>>> result = new HashMap<Class<?>, Set<Class<?>>>();
        result.put(TransactionProxy.class, ClassContext.getServices());
        return result;
    }

    private static Map<Class<?>, Set<Class<?>>> getAspectProxy2BeanClassesMap(){
        Map<Class<?>, Set<Class<?>>> result = new HashMap<Class<?>, Set<Class<?>>>();
        Set<Class<?>> aspectProxySubClasses = ClassContext.getSubClasses(AspectProxy.class);
        for(Class<?> aspectProxySubClass:aspectProxySubClasses){
            if(aspectProxySubClass.isAnnotationPresent(Aspect.class)){
                Aspect aspect = aspectProxySubClass.getAnnotation(Aspect.class);
                Set<Class<?>> aspectValueBeanClasses = getAspectValueBeanClasses(aspect);
                result.put(aspectProxySubClass, aspectValueBeanClasses);
            }
        }
        return result;
    }
    
    private static Set<Class<?>> getAspectValueBeanClasses(Aspect aspect){
        Set<Class<?>> classes = new HashSet<Class<?>>();
        Class<? extends Annotation> annotation = aspect.value();
        if(annotation != null && !annotation.equals(Aspect.class)){
            classes.addAll(ClassContext.getClassesByAnnotation(annotation));
        }
        return classes;
    }
    
    private static Map<Class<?>, List<Proxy>> parseBeanClass2ProxyListMap(Map<Class<?>, Set<Class<?>>> proxy2BeanClassesMap){
        Map<Class<?>, List<Proxy>> result = new HashMap<Class<?>, List<Proxy>>();
        for(Map.Entry<Class<?>, Set<Class<?>>> entry:proxy2BeanClassesMap.entrySet()){
            Class<?> aspectProxyClass = entry.getKey();
            Set<Class<?>> effectClasses = entry.getValue();
            for(Class<?> claszz:effectClasses){
                Proxy proxy;
                try {
                    proxy = (Proxy)aspectProxyClass.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                    LOGGER.error("newInstance proxy error", e);
                    throw new RuntimeException(e);
                } 
                
                if(result.containsKey(claszz)){
                    result.get(claszz).add(proxy);
                }else{
                    List<Proxy> proxyList = new ArrayList<Proxy>();
                    proxyList.add(proxy);
                    result.put(claszz, proxyList);
                }
            }
        }
        return result;
    }
    
}
