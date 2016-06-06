package com.why.ismart.framework.aop;

import java.lang.reflect.Method;

public class AspectProxy implements Proxy {

    @Override
    public Object doProxy(ProxyChain proxyChain) {
        Class<?> clazz = proxyChain.getTargetClass();
        Method method = proxyChain.getTargetMethod();
        Object[] params = proxyChain.getMethodParams();
        
        begin();
        Object result;
        if(intercept(clazz, method, params)){
            before(clazz, method, params);
            result = proxyChain.doProxyChain();
            after(clazz, method, params, result);
        }else{
            result = proxyChain.doProxyChain();
        }
        end();
        
        return result;
    }
    
    public void begin() {
        
    }
    
    /** 是否进行切面代理拦截，返回true代表拦截并可在before和after做具体拦截逻辑 */
    public boolean intercept(Class<?> clazz, Method method, Object[] params) {
        return true;
    }
    
    public void before(Class<?> clazz, Method method, Object[] params) {
        
    }

    public void after(Class<?> clazz, Method method, Object[] params, Object result) {
        
    }

    public void end() {
        
    }

}
