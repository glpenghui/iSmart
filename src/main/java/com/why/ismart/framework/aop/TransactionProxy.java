package com.why.ismart.framework.aop;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.why.ismart.framework.util.JdbcUtil;

public class TransactionProxy implements Proxy {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionProxy.class);
    
    private static final ThreadLocal<Boolean> THREAD_FLAG = new ThreadLocal<Boolean>(){
        protected Boolean initialValue() {
            return false;
        };
    };
    
    @Override
    public Object doProxy(ProxyChain proxyChain) throws Throwable {
        Object result;
        boolean flag = THREAD_FLAG.get();
        Method method = proxyChain.getTargetMethod();
        if(!flag && method.isAnnotationPresent(Transaction.class)){
            THREAD_FLAG.set(true);
            try{
                JdbcUtil.beginTransaction();
                LOGGER.info("begin transaction");
                result = proxyChain.doProxyChain();
                JdbcUtil.commitTransaction();
                LOGGER.info("commit transaction");
            }catch(Throwable e){
                e.printStackTrace();
                JdbcUtil.rollbackTransaction();
                LOGGER.error("TransactionProxy doProxy error, rollback transaction", e);
                throw e;
            }finally{
                THREAD_FLAG.remove();
                JdbcUtil.releaseConnection();
                LOGGER.info("release connection");
            }
        }else{
            result = proxyChain.doProxyChain();
        }
        return result;
    }

}
