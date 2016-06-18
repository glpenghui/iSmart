package com.why.ismart.framework.util;

import java.util.Collection;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;

/**
 * 依赖org.apache.commons.collections4的CollectionUtils和MapUtils
 * 
 * @author whg
 * @date 2016年6月18日 下午3:59:55
 */
public class CollectionUtil {

    public static boolean isEmpty(Collection<?> coll){
        return CollectionUtils.isEmpty(coll);
    }
    
    public static boolean isNotEmpty(Collection<?> coll){
        return !isEmpty(coll);
    }
    
    public static boolean isEmpty(Map<?, ?> map){
        return MapUtils.isEmpty(map);
    }
    
    public static boolean isNotEmpty(Map<?, ?> map){
        return !isEmpty(map);
    }
    
}
