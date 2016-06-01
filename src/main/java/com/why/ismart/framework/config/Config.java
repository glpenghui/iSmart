package com.why.ismart.framework.config;

public class Config {

    private static final String CONFIG_FILE = "ismart.properties";
    
    private static final String JDBC_DRIVER = "ismart.jdbc.driver";
    private static final String JDBC_URL = "ismart.jdbc.url";
    private static final String JDBC_USER_NAME = "ismart.jdbc.username";
    private static final String JDBC_PASSWORD = "ismart.jdbc.password";
    
    private static final String APP_BASE_PACKAGE = "ismart.app.basePackage";
    private static final String APP_JSP_PATH = "ismart.app.jspPath";
    private static final String APP_ASSERT_PATH = "ismart.app.assetPath";
    
    private static final Property p = new Property(CONFIG_FILE);
    
    public static String jdbcDriver(){
        return p.getStr(JDBC_DRIVER);
    }
    
    public static String jdbcUrl(){
        return p.getStr(JDBC_URL);
    }
    
    public static String jdbcUserName(){
        return p.getStr(JDBC_USER_NAME);
    }
    
    public static String jdbcPassword(){
        return p.getStr(JDBC_PASSWORD);
    }
    
    public static String appBasePackage(){
        return p.getStr(APP_BASE_PACKAGE);
    }
    
    public static String appJspPath(){
        return p.getStr(APP_JSP_PATH);
    }
    
    public static String appAssertPath(){
        return p.getStr(APP_ASSERT_PATH);
    }
    
}
