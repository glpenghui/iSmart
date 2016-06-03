package com.why.ismart.framework.mvc;

import java.lang.reflect.Method;

public class Handler {

    private final Method method;
    
    public Handler(Method method) {
        this.method = method;
    }

    public Class<?> getController() {
        return method.getDeclaringClass();
    }

    public Method getMethod() {
        return method;
    }

    @Override
    public String toString() {
        return "Handler [method=" + method + "]";
    }
    
}
