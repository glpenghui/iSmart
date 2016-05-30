package com.why.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.why.domain.Customer;
import com.why.service.CustomerService;

@SuppressWarnings("serial")
@WebServlet("/customer")
public class CustomerServlet extends HttpServlet {

    private CustomerService customerService;
    
    @Override
    public void init() throws ServletException {
        customerService = new CustomerService();
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       List<Customer> customerList = customerService.findCustomerList();
       req.setAttribute("customerList", customerList);
       req.getRequestDispatcher("/WEB-INF/jsp/customer/customer.jsp").forward(req, resp);
    }
    
}
