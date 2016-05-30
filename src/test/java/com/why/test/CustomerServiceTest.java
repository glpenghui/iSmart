package com.why.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.why.domain.Customer;
import com.why.service.CustomerService;

public class CustomerServiceTest {

    private final CustomerService customerService = new CustomerService();
    
    @Before
    public void init(){
        
    }
    
    @Test
    public void testFindCustomers(){
        List<Customer> customers = customerService.findCustomerList();
        Assert.assertTrue(customers.size() == 2);
    }
    
    @Ignore
    @Test
    public void testCreateCustomer(){
        Map<String, Object> fieldMap = new HashMap<String, Object>();
        fieldMap.put("name", "why");
        fieldMap.put("contact", "为什么");
        fieldMap.put("telephone", "12345678");
        Assert.assertTrue(customerService.createCustomer(fieldMap));
    }
    
}
