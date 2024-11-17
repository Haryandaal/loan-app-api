package com.loan.service;

import com.loan.dto.CustomerResponse;
import com.loan.dto.CustomerRequest;
import com.loan.entity.Customer;

import java.util.List;

public interface CustomerService {

    CustomerResponse create(CustomerRequest request);

    List<CustomerResponse> getAll();

    Customer getById(String id);

    CustomerResponse getOne(String id);

    CustomerResponse update(String id, CustomerRequest request);

    void deleteById(String id);

}
