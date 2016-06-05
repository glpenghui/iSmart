package com.why.ismart.framework.mvc;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.why.ismart.framework.aop.AopContext;
import com.why.ismart.framework.ioc.ClassContext;
import com.why.ismart.framework.util.ArrayUtil;

public class ControllerContext {

    private static final Logger LOGGER = LoggerFactory.getLogger(ControllerContext.class);
    
    private static final Map<Request, Handler> ACTION_MAP;
    static{
        LOGGER.info("ControllerContext static init...");
        ACTION_MAP = new HashMap<Request, Handler>();
        Set<Class<?>> controllers = ClassContext.getControllers();
        for(Class<?> controller:controllers){
            Method[] methods = controller.getDeclaredMethods();
            for(Method method:methods){
                if(method.isAnnotationPresent(Action.class)){
                    Action action = method.getAnnotation(Action.class);
                    String mapping = action.value();
                    if(mapping.matches("\\w+:/\\w*")){
                        String[] array = mapping.split(":");
                        if(ArrayUtil.isNotEmpty(array) && array.length == 2){
                            String reqMethod = array[0];
                            String reqPath = array[1];
                            Request request = new Request(reqMethod, reqPath);
                            Handler handler = new Handler(controller, method);
                            ACTION_MAP.put(request,  handler);
                        }
                    }
                }
            }
        }
    }
    
    public static Handler getHandler(String reqMethod, String reqPath){
        return getHandler(new Request(reqMethod, reqPath));
    }
    
    public static Handler getHandler(Request request){
        return ACTION_MAP.get(request);
    }
    
}
