package com.why.ismart.framework.util;

import org.apache.commons.lang3.StringUtils;

/**
 * 依赖org.apache.commons.lang3的StringUtils
 * 
 * @author whg
 * @date 2016年6月18日 下午4:04:35
 */
public class StringUtil {
    
    public static boolean isEmpty(String str) {
        if (str != null) {
            str = str.trim();
        }
        return StringUtils.isEmpty(str);
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }
    
    public static String[] splitString(String str, String separator){
        return StringUtils.splitByWholeSeparator(str, separator);
    }
    
}
