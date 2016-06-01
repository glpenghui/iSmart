package com.why.ismart.framework.util;

import org.apache.commons.lang3.ArrayUtils;

public class ArrayUtil {

    public static boolean isEmpty(Object[] array){
        return ArrayUtils.isEmpty(array);
    }
    
    public static boolean isNotEmpty(Object[] array){
        return !isEmpty(array);
    }
    
}
