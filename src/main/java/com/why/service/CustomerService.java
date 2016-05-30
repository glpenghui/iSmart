package com.why.service;

import java.util.List;
import java.util.Map;

import com.why.domain.Customer;
import com.why.repo.JdbcHelper;

public class CustomerService {
    
    public List<Customer> findCustomerList(){
        return JdbcHelper.queryEntityList("SELECT * FROM customer", Customer.class);
    }
    
    public Customer findCustomer(long id){
        return JdbcHelper.queryEntity("SELECT * FROM customer WHERE id=?", Customer.class, id);
    }
    
    public boolean createCustomer(Map<String, Object> fieldMap){
        return JdbcHelper.insertEntity(fieldMap, Customer.class);
    }
    
    public boolean updateCustomer(long id, Map<String, Object> fieldMap){
        return JdbcHelper.updateEntity(id, fieldMap, Customer.class);
    }
    
    public boolean deleteCustomer(long id){
        return JdbcHelper.deleteEntity(id, Customer.class);
    }
    
}
