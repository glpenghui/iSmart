package com.why.ismart.framework.mvc;

import java.lang.reflect.Method;

public class Handler {

    private final Class<?> controller;
    private final Method method;
    
    public Handler(Class<?> controller, Method method) {
        this.controller = controller;
        this.method = method;
    }

    public Class<?> getController() {
        return controller;
    }

    public Method getMethod() {
        return method;
    }

    @Override
    public String toString() {
        return "Handler [method=" + method + "]";
    }
    
}
