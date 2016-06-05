package com.why.ismart.framework.aop;

import java.lang.reflect.Method;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.cglib.proxy.MethodProxy;

public class ProxyChain {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProxyChain.class);
    
    private final Class<?> targetClass;
    private final Object targetObject;
    
    private final Method targetMethod;
    private final Object[] methodParams;
    private final MethodProxy methodProxy;
    
    private final List<Proxy> proxyList;
    private int proxyIndex;
    
    public ProxyChain(Class<?> targetClass, Object targetObject, Method targetMethod, 
            Object[] methodParams, MethodProxy methodProxy, List<Proxy> proxyList) {
        this.targetClass = targetClass;
        this.targetObject = targetObject;
        this.targetMethod = targetMethod;
        this.methodParams = methodParams;
        this.methodProxy = methodProxy;
        this.proxyList = proxyList;
        this.proxyIndex = 0;
    }
    
    public Object doProxyChain(){
        if(proxyIndex < proxyList.size()){
            return proxyList.get(proxyIndex++).doProxy(this);
        }else{
            try {
                return methodProxy.invokeSuper(targetObject, methodParams);
            } catch (Throwable e) {
                e.printStackTrace();
                LOGGER.error("doProxyChain error", e);
                throw new RuntimeException(e);
            }
        }
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public Method getTargetMethod() {
        return targetMethod;
    }

    public Object[] getMethodParams() {
        return methodParams;
    }
    
}
