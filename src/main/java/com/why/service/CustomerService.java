package com.why.service;

import java.sql.Connection;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.why.domain.Customer;
import com.why.repo.JdbcHelper;

public class CustomerService {
    
    public List<Customer> findCustomerList(){
        List<Customer> customers = Collections.emptyList();
        Connection conn = null;
        try {
            conn = JdbcHelper.getConnection();
            customers = JdbcHelper.queryEntityList(conn, "SELECT * FROM customer", Customer.class);
        } finally {
            JdbcHelper.closeConnection(conn);
        }
        System.out.println(Arrays.toString(customers.toArray()));
        return customers;
    }
    
    public boolean createCustomer(Map<String, Object> fieldMap){
        return false;
    }
    
}
