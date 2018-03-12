package org.smart4j.chapter1.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.smart4j.chapter1.model.Customer;
import org.smart4j.chapter1.service.CustomerService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerServiceTest {
    private final CustomerService customerService;

    public CustomerServiceTest() {
        this.customerService = new CustomerService();
    }

    @Before
    public void init() {
        // 初始化数据库
    }

    @Test
    public void getCustomerListTest() throws Exception {
        List<Customer> customers = customerService.getCustomerList();
        Assert.assertEquals(4, customers.size());
    }

    @Test
    public void createCustomerTest() throws Exception {
        Map<String, Object> fieldMap = new HashMap<>();
        fieldMap.put("name", "hello");
        fieldMap.put("contact", "jack");
        fieldMap.put("telephone", "18710111111");
        boolean flag = customerService.createCustomer(fieldMap);
        Assert.assertTrue(flag);
    }

    @Test
    public void deleteCustomerTest() throws Exception {
        long id = 2;
        boolean flag = customerService.deleteCustomer(id);
        Assert.assertTrue(flag);
    }

}