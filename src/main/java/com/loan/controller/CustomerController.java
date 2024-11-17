package com.loan.controller;

import com.loan.dto.CustomerRequest;
import com.loan.dto.CustomerResponse;
import com.loan.dto.WebResponse;
import com.loan.service.CustomerService;
import com.loan.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<WebResponse<CustomerResponse>> register(@RequestBody CustomerRequest customerRequest) {
        CustomerResponse response = customerService.create(customerRequest);
        return ResponseUtil.buildResponse(HttpStatus.CREATED, "Customer created", response);
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<WebResponse<CustomerResponse>> getCustomerById(@PathVariable(name = "id") String id) {
        CustomerResponse response = customerService.getOne(id);
        return ResponseUtil.buildResponse(HttpStatus.OK, "Customer found", response);
    }

    @PutMapping("{id}")
    public ResponseEntity<WebResponse<CustomerResponse>> updateCustomer(@PathVariable(name = "id") String id, @RequestBody CustomerRequest request) {
        CustomerResponse response = customerService.update(id, request);
        return ResponseUtil.buildResponse(HttpStatus.OK, "Customer updated", response);
    }

    @GetMapping
    public ResponseEntity<WebResponse<List<CustomerResponse>>> getAllCustomer() {
        List<CustomerResponse> responses = customerService.getAll();
        return ResponseUtil.buildResponse(HttpStatus.OK, "Customers found", responses);
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<WebResponse<String>> deleteCustomerById(@PathVariable(name = "id") String id) {
        customerService.deleteById(id);
        return ResponseUtil.buildResponse(HttpStatus.OK, "Customer deleted", null);
    }
}
