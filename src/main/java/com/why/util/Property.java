package com.why.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Property {

    private static final Logger LOGGER = LoggerFactory.getLogger(Property.class);
    
    private final Properties properties;
    
    public Property(String path){
        properties = load(path);
    }
    
    private Properties load(String path){
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
    
    public String getStr(String key){
        return properties.getProperty(key);
    }
    
    public int getInt(String key){
        return Integer.parseInt(getStr(key));
    }
    
    public boolean getBool(String key){
        return Boolean.valueOf(getStr(key));
    }
    
}
