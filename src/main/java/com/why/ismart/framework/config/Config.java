package com.why.ismart.framework.config;

public class Config {

    private static final String CONFIG_FILE = "ismart.properties";
    
    private static final String JDBC_DRIVER = "ismart.jdbc.driver";
    private static final String JDBC_URL = "ismart.jdbc.url";
    private static final String JDBC_USER_NAME = "ismart.jdbc.username";
    private static final String JDBC_PASSWORD = "ismart.jdbc.password";
    
    private static final String POOL_INITIALSIZE = "ismart.pool.initialSize";
    private static final String POOL_MINIDLE =  "ismart.pool.minIdle";
    private static final String POOL_MAXTOTAL = "ismart.pool.maxTotal";
    private static final String POOL_MAXWAITMILLIS = "ismart.pool.maxWaitMillis";
    private static final String POOL_TESTWHILEIDLE = "ismart.pool.testWhileIdle";
    private static final String POOL_VALIDATIONQUERY = "ismart.pool.validationQuery";
    private static final String POOL_REMOVEABANDONEDONBORROW = "ismart.pool.removeAbandonedOnBorrow";
    
    private static final String APP_BASE_PACKAGE = "ismart.app.basePackage";
    private static final String APP_JSP_PATH = "ismart.app.jspPath";
    private static final String APP_ASSET_PATH = "ismart.app.assetPath";
    
    private static final Property p = new Property(CONFIG_FILE);
    
    public static String getJdbcDriver(){
        return p.getStr(JDBC_DRIVER);
    }
    
    public static String getJdbcUrl(){
        return p.getStr(JDBC_URL);
    }
    
    public static String getJdbcUserName(){
        return p.getStr(JDBC_USER_NAME);
    }
    
    public static String getJdbcPassword(){
        return p.getStr(JDBC_PASSWORD);
    }
    
    public static int getPoolInitialSize(){
        return p.getInt(POOL_INITIALSIZE);
    }
    
    public static int getPoolMinIdle(){
        return p.getInt(POOL_MINIDLE);
    }
    
    public static int getPoolMaxTotal(){
        return p.getInt(POOL_MAXTOTAL);
    }
    
    public static int getPoolMaxWaitMillis(){
        return p.getInt(POOL_MAXWAITMILLIS);
    }
    
    public static boolean getPoolTestWhileIdle(){
        return p.getBool(POOL_TESTWHILEIDLE);
    }
    
    public static String getPoolValidationQuery(){
        return p.getStr(POOL_VALIDATIONQUERY);
    }
    
    public static boolean getPoolRemoveAbandonedOnBorrow(){
        return p.getBool(POOL_REMOVEABANDONEDONBORROW);
    }
    
    public static String getAppBasePackage(){
        return p.getStr(APP_BASE_PACKAGE);
    }
    
    public static String getAppJspPath(){
        return p.getStr(APP_JSP_PATH);
    }
    
    public static String getAppAssetPath(){
        return p.getStr(APP_ASSET_PATH);
    }
    
}
