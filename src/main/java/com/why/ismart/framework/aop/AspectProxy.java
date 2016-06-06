package com.why.ismart.framework.aop;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AspectProxy implements Proxy {

    private static final Logger LOGGER = LoggerFactory.getLogger(AspectProxy.class);
    
    @Override
    public Object doProxy(ProxyChain proxyChain) throws Throwable{
        Object result;
        Class<?> clazz = proxyChain.getTargetClass();
        Method method = proxyChain.getTargetMethod();
        Object[] params = proxyChain.getMethodParams();
        
        begin();
        try{
            if(intercept(clazz, method, params)){
                before(clazz, method, params);
                result = proxyChain.doProxyChain();
                after(clazz, method, params, result);
            }else{
                result = proxyChain.doProxyChain();
            }
        }catch(Throwable e){
            e.printStackTrace();
            LOGGER.error("doProxy error", e);
            error(clazz, method, params, e);
            throw e;
        }finally{
            end();
        }
        
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
    
    private void error(Class<?> clazz, Method method, Object[] params, Throwable e) {
        
    }

    public void end() {
        
    }

}
