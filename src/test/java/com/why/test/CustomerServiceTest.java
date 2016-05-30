package com.why.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.why.domain.Customer;
import com.why.repo.JdbcHelper;
import com.why.service.CustomerService;

public class CustomerServiceTest {

    private static final AtomicInteger count = new AtomicInteger();
    private final CustomerService customerService = new CustomerService();
    
    @Before
    public void init(){
        JdbcHelper.executeSqlFile("sql/insert_test_customer.sql");
        System.out.println("init"+count.incrementAndGet());
    }
    
    @Test
    public void testFindCustomers(){
        List<Customer> customers = customerService.findCustomerList();
        Assert.assertTrue(customers.size() == 2);
        System.out.println("testFindCustomers");
    }
    
    @Test
    public void testFindCustomer() {
        long id = 1;
        Customer customer = customerService.findCustomer(id);
        Assert.assertNotNull(customer);
        System.out.println("testFindCustomer");
    }
    
    @Test
    public void testCreateCustomer(){
        Map<String, Object> fieldMap = new HashMap<String, Object>();
        fieldMap.put("name", "why");
        fieldMap.put("contact", "为什么");
        fieldMap.put("telephone", "12345678");
        Assert.assertTrue(customerService.createCustomer(fieldMap));
        Customer customer = customerService.findCustomer(3);
        Assert.assertTrue(customer.getContact().equals("为什么"));
        System.out.println("testCreateCustomer");
    }
    
    @Test
    public void testUpdateCustomer() {
        long id = 1;
        Map<String, Object> fieldMap = new HashMap<String, Object>();
        fieldMap.put("contact", "Eric");
        boolean result = customerService.updateCustomer(id, fieldMap);
        Assert.assertTrue(result);
        System.out.println("testUpdateCustomer");
    }
    
    @Test
    public void testDeleteCustomer() {
        long id = 1;
        boolean result = customerService.deleteCustomer(id);
        Assert.assertTrue(result);
        System.out.println("testDeleteCustomer");
    }
    
}
