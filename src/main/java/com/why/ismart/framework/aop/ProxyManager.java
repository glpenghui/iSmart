package com.why.ismart.framework.aop;

import java.lang.reflect.Method;
import java.util.List;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class ProxyManager {

    @SuppressWarnings("unchecked")
    public static <T> T createProxy(Class<T> targetClass, List<Proxy> proxyList){
        return (T)Enhancer.create(targetClass, new ProxyChainInterceptor(targetClass, proxyList));
    }
    
    private static final class ProxyChainInterceptor implements MethodInterceptor{
        
        private final Class<?> targetClass;
        private final List<Proxy> proxyList;
        
        public ProxyChainInterceptor(Class<?> targetClass, List<Proxy> proxyList) {
            this.targetClass = targetClass;
            this.proxyList = proxyList;
        }

        @Override
        public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
            return new ProxyChain(targetClass, obj, method, args, proxy, proxyList).doProxyChain();
        }
    }
    
}
