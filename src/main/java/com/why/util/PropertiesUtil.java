package com.why.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertiesUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesUtil.class);
    
    public static Properties load(String path){
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream input = loader.getResourceAsStream(path);
        if(input == null){
            throw new IllegalArgumentException("找不到配置文件"+path);
        }
        
        Properties properties = new Properties();
        try {
            properties.load(input);
        }catch(IOException e) {
            e.printStackTrace();
            LOGGER.error("加载"+path+"属性文件失败", e);
        }finally{
            if(input != null){
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return properties;
    }
    
}
