package com.why.ismart.test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class DynamicProxy<T> implements InvocationHandler {

    private final T target;
    
    public DynamicProxy(T target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args){
        try {
            before();
            Object result = method.invoke(target, args);
            after();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } 
    }
    
    @SuppressWarnings("unchecked")
    public T getProxy(){
        Class<?> clazz = target.getClass();
        return (T)Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(), this);
    }
    
    public void before(){
        System.out.println("DynamicProxy before");
    }
    
    public void after(){
        System.out.println("DynamicProxy after");
    }

}
