package com.why.ismart.framework;

import com.why.ismart.framework.aop.AopContext;
import com.why.ismart.framework.ioc.BeanContext;
import com.why.ismart.framework.ioc.ClassContext;
import com.why.ismart.framework.ioc.IocContext;
import com.why.ismart.framework.mvc.ControllerContext;
import com.why.ismart.framework.util.ClassUtil;

public class Loader {

    public static void init(){
        Class<?>[] classes = {
            ClassContext.class,
            BeanContext.class,
            AopContext.class,
            IocContext.class,
            ControllerContext.class
        };
        for(Class<?> clazz:classes){
            ClassUtil.loadClass(clazz.getName());
        }
    }
    
}
