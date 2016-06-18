package com.why.ismart.framework.util;

import org.apache.commons.lang3.ArrayUtils;

/**
 * 依赖org.apache.commons.lang3的ArrayUtils
 * 
 * @author whg
 * @date 2016年6月18日 下午4:00:49
 */
public class ArrayUtil {

    public static boolean isEmpty(Object[] array){
        return ArrayUtils.isEmpty(array);
    }
    
    public static boolean isNotEmpty(Object[] array){
        return !isEmpty(array);
    }
    
}
