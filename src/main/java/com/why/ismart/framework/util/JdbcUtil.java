package com.why.ismart.framework.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.why.ismart.framework.config.Config;

/**
 * 数据库工具类
 * <ul>
 * <li>依赖org.apache.commons.dbcp2的BasicDataSource做数据库连接池化</li>
 * <li>依赖org.apache.commons.dbutils包封装了一系列jdbc数据库操作</li>
 * </ul>
 * 
 * @author whg
 * @date 2016年6月18日 下午4:26:16
 */
public class JdbcUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcUtil.class);
    
    private static final BasicDataSource DATA_SOURCE;
    static{
        DATA_SOURCE = new BasicDataSource();
        DATA_SOURCE.setDriverClassName(Config.getJdbcDriver());
        DATA_SOURCE.setUrl(Config.getJdbcUrl());
        DATA_SOURCE.setUsername(Config.getJdbcUserName());
        DATA_SOURCE.setPassword(Config.getJdbcPassword());
        
        DATA_SOURCE.setInitialSize(Config.getPoolInitialSize());
        DATA_SOURCE.setMinIdle(Config.getPoolMinIdle());
        DATA_SOURCE.setMaxTotal(Config.getPoolMaxTotal());
        DATA_SOURCE.setMaxWaitMillis(Config.getPoolMaxWaitMillis());
        DATA_SOURCE.setTestWhileIdle(Config.getPoolTestWhileIdle());
        DATA_SOURCE.setValidationQuery(Config.getPoolValidationQuery());
        DATA_SOURCE.setRemoveAbandonedOnBorrow(Config.getPoolRemoveAbandonedOnBorrow());
    }
    
    private static final ThreadLocal<Connection> THREAD_CONNECTION = new ThreadLocal<Connection>();
    private static final QueryRunner QUERY_RUNNER = new QueryRunner();
    
    public static <T> boolean insertEntity(Map<String, Object> fieldMap, Class<T> entityClass){
        if(CollectionUtil.isEmpty(fieldMap)){
            LOGGER.error("insertEntity fieldMap is empty");
            return false;
        }
        
        StringBuilder sql = new StringBuilder("INSERT INTO "+getTable(entityClass));
        StringBuilder columns = new StringBuilder("(");
        StringBuilder values = new StringBuilder("(");
        for(String fieldName:fieldMap.keySet()){
            columns.append(fieldName).append(", ");
            values.append("?, ");
        }
        columns.replace(columns.lastIndexOf(", "), columns.length(), ")");
        values.replace(values.lastIndexOf(", "), values.length(), ")");
        sql.append(columns).append(" VALUES ").append(values);
        
        Object[] params = fieldMap.values().toArray();
        return executeUpdate(sql.toString(), params) == 1;
    }
    
    public static <T> boolean updateEntity(long id, Map<String, Object> fieldMap, Class<T> entityClass){
        if(CollectionUtil.isEmpty(fieldMap)){
            LOGGER.error("updateEntity fieldMap is empty");
            return false;
        }
        
        StringBuilder sql = new StringBuilder("UPDATE "+getTable(entityClass)+" SET ");
        StringBuilder columns = new StringBuilder();
        for(String fieldName:fieldMap.keySet()){
            columns.append(fieldName).append("=?, ");
        }
        sql.append(columns.substring(0, columns.lastIndexOf(", "))).append(" WHERE id=?");
        
        List<Object> paramList = new ArrayList<Object>();
        paramList.addAll(fieldMap.values());
        paramList.add(id);
        
        Object[] params = paramList.toArray();
        return executeUpdate(sql.toString(), params) == 1;
    }
    
    public static <T> boolean deleteEntity(long id, Class<T> entityClass){
        String sql = "DELETE FROM "+getTable(entityClass)+" WHERE id=?";
        return executeUpdate(sql.toString(), id) == 1;
    }
    
    private static String getTable(Class<?> entityClass) {
        return entityClass.getSimpleName();
    }
    
    public static void executeSqlFile(String path) {
        InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        try {
            String sql;
            while((sql=reader.readLine()) != null){
                executeUpdate(sql);
            }
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("executeSqlFile error", e);
            throw new RuntimeException(e);
        }
    }

    public static int executeUpdate(String sql, Object... params){
        try {
            return QUERY_RUNNER.update(getConnection(), sql, params);
        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.error("executeUpdate error", e);
            throw new RuntimeException(e);
        }
    }
    
    public static <T> T queryEntity(String sql, Class<T> entityClass, Object... params){
        try {
            return QUERY_RUNNER.query(getConnection(), sql, new BeanHandler<T>(entityClass), params);
        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.error("queryEntity error", e);
            throw new RuntimeException(e);
        }
    }
    
    public static <T> List<T> queryEntityList(String sql, Class<T> entityClass, Object... params){
        try {
            return QUERY_RUNNER.query(getConnection(), sql, new BeanListHandler<T>(entityClass), params);
        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.error("queryEntityList error", e);
            throw new RuntimeException(e);
        }
    }
    
    public static List<Map<String, Object>> executeQuery(String sql, Object... params){
        try {
            return QUERY_RUNNER.query(getConnection(), sql, new MapListHandler(), params);
        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.error("executeQuery error", e);
            throw new RuntimeException(e);
        }
    }
    
    public static void beginTransaction(){
        Connection conn = getConnection();
        try {
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.error("beginTransaction error", e);
            throw new RuntimeException(e);
        }
    }
    
    public static void commitTransaction(){
        Connection conn = getConnection();
        try {
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.error("commitTransaction error", e);
            throw new RuntimeException(e);
        }
    }
    
    public static void rollbackTransaction(){
        Connection conn = getConnection();
        try {
            conn.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.error("rollbackTransaction error", e);
            throw new RuntimeException(e);
        }
    }
    
    /**
     * actually, QueryRunner close the Connection internally
     */
    private static Connection getConnection(){
        Connection conn = THREAD_CONNECTION.get();
        if(conn == null){
            try {
                conn = DATA_SOURCE.getConnection();
            } catch (SQLException e) {
                e.printStackTrace();
                LOGGER.error("getConnection error", e);
                throw new RuntimeException(e);
            } finally {
                THREAD_CONNECTION.set(conn);
            }
        }
        AssertUtil.notNull(conn);
        return conn;
    }
    
    /**
     * conn.close() it meant to put the Connection back to the pool, not really close it
     */
    public static void releaseConnection(){
        Connection conn = THREAD_CONNECTION.get();
        if(conn != null){
            try {
                if(!conn.isClosed()){
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                LOGGER.error("releaseConnection error", e);
                throw new RuntimeException(e);
            } finally {
                THREAD_CONNECTION.remove();
            }
        }
    }
    
}
