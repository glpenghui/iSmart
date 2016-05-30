package com.why.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.why.domain.Customer;
import com.why.repo.JdbcHelper;

public class CustomerService {
    
    public List<Customer> findCustomerList(){
        List<Customer> customers = new ArrayList<Customer>();
        Connection conn = null;
        try {
            conn = JdbcHelper.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM customer");
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                Customer customer = new Customer();
                customer.setId(rs.getLong("id"));
                customer.setName(rs.getString("name"));
                customer.setContact(rs.getString("contact"));
                customer.setTelephone(rs.getString("telephone"));
                customer.setEmail(rs.getString("email"));
                customer.setRemark(rs.getString("remark"));
                customers.add(customer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
