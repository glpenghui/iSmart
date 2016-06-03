package com.why.ismart.framework.util;

import java.net.URLDecoder;
import java.net.URLEncoder;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UrlCoderUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(UrlCoderUtil.class);

    public static String encodeURL(String source) {
        try {
            return URLEncoder.encode(source, "UTF-8");
        } catch (Exception e) {
            LOGGER.error("encode url error", e);
            throw new RuntimeException(e);
        }
    }

    public static String decodeURL(String source) {
        try {
            return URLDecoder.decode(source, "UTF-8");
        } catch (Exception e) {
            LOGGER.error("decode url error", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * MD5 加密
     */
    public static String md5(String source) {
        return DigestUtils.md5Hex(source);
    }
    
}
