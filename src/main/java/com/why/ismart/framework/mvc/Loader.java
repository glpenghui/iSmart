package com.why.ismart.framework.mvc;

import com.why.ismart.framework.ioc.BeanContext;
import com.why.ismart.framework.ioc.ClassContext;
import com.why.ismart.framework.ioc.Ioc;
import com.why.ismart.framework.mvc.ControllerContext;
import com.why.ismart.framework.util.ClassUtil;

public class Loader {

    public static void init(){
        Class<?>[] classes = {
            ClassContext.class,
            BeanContext.class,
            Ioc.class,
            ControllerContext.class
        };
        for(Class<?> clazz:classes){
            ClassUtil.loadClass(clazz.getName());
        }
    }
    
}
