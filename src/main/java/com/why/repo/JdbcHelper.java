package com.why.repo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.why.util.CollectionUtil;
import com.why.util.Property;

public class JdbcHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcHelper.class);
    
    private static final BasicDataSource DATA_SOURCE;
    
    static{
        Property property = new Property("jdbc.properties");
        
        DATA_SOURCE = new BasicDataSource();
        DATA_SOURCE.setDriverClassName(property.getStr("jdbc.driver"));
        DATA_SOURCE.setUrl(property.getStr("jdbc.url"));
        DATA_SOURCE.setUsername(property.getStr("jdbc.username"));
        DATA_SOURCE.setPassword(property.getStr("jdbc.password"));
        
        DATA_SOURCE.setInitialSize(property.getInt("pool.initialSize"));
        DATA_SOURCE.setMinIdle(property.getInt("pool.minIdle"));
        DATA_SOURCE.setMaxTotal(property.getInt("pool.maxTotal"));
        DATA_SOURCE.setMaxWaitMillis(property.getInt("pool.maxWaitMillis"));
        DATA_SOURCE.setTestWhileIdle(property.getBool("pool.testWhileIdle"));
        DATA_SOURCE.setValidationQuery(property.getStr("pool.validationQuery"));
        DATA_SOURCE.setRemoveAbandonedOnBorrow(property.getBool("pool.removeAbandonedOnBorrow"));
    }
    
    private static final QueryRunner QUERY_RUNNER = new QueryRunner();
    
    private static final ThreadLocal<Connection> THREAD_CONNECTION_MAP = new ThreadLocal<Connection>();
    
    public static <T> T queryEntity(String sql, Class<T> entityClass, Object... params){
        T entity;
        Connection conn = getConnection();
        try {
            entity = QUERY_RUNNER.query(conn, sql, new BeanHandler<T>(entityClass), params);
        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.error("queryEntity error", e);
            throw new RuntimeException(e);
        } finally {
            closeConnection();
        }
        return entity;
    }
    
    public static <T> List<T> queryEntityList(String sql, Class<T> entityClass, Object... params){
        List<T> entityList;
        Connection conn = getConnection();
        try {
            entityList = QUERY_RUNNER.query(conn, sql, new BeanListHandler<T>(entityClass), params);
        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.error("queryEntityList error", e);
            throw new RuntimeException(e);
        } finally {
            closeConnection();
        }
        return entityList;
    }
    
    public static List<Map<String, Object>> executeQuery(String sql, Object... params){
        List<Map<String, Object>> result;
        Connection conn = getConnection();
        try {
            result = QUERY_RUNNER.query(conn, sql, new MapListHandler(), params);
        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.error("executeQuery error", e);
            throw new RuntimeException(e);
        } finally {
            closeConnection();
        }
        return result;
    }
    
    /** 
     * Don't need close Connection because QueryRunner close Internal<br>
     * ThreadLocal hold Connection for per Thread and don't remove!?
     */
    private static Connection getConnection(){
        Connection conn = THREAD_CONNECTION_MAP.get();
        if(conn == null){
            try {
                conn = DATA_SOURCE.getConnection();
            } catch (SQLException e) {
                e.printStackTrace();
                LOGGER.error("getConnection error", e);
                throw new RuntimeException(e);
            } finally {
                THREAD_CONNECTION_MAP.set(conn);
            }
        }
        return conn;
    }
    
    private static void closeConnection(){
        Connection conn = THREAD_CONNECTION_MAP.get();
        if(conn == null){
            return;
        }
        
        try {
            conn.close(); //use datasource not close just put back to pool
        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.error("closeConnection error", e);
            throw new RuntimeException(e);
        } finally {
            THREAD_CONNECTION_MAP.remove();
        }
    }
    
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

    public static int executeUpdate(String sql, Object... params){
        int rows;
        Connection conn = getConnection();
        try {
            rows = QUERY_RUNNER.update(conn, sql, params);
        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.error("executeUpdate error", e);
            throw new RuntimeException(e);
        }
        return rows;
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
    
}
