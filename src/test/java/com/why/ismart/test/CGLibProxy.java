package com.why.ismart.test;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class CGLibProxy implements MethodInterceptor{

    public static final CGLibProxy instance = new CGLibProxy();
    
    private CGLibProxy(){
        
    }
    
    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy){
        try {
            before();
            Object result = proxy.invokeSuper(obj, args);
            after();
            return result;
        } catch (Throwable e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    
    @SuppressWarnings("unchecked")
    public static <T> T getProxy(Class<T> clazz){
        return (T) Enhancer.create(clazz, instance);
    }
    
    public void before(){
        System.out.println("CGLibProxy before");
    }
    
    public void after(){
        System.out.println("CGLibProxy after");
    }

}
