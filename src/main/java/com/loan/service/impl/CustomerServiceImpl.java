package com.loan.service.impl;

import com.loan.dto.CustomerRequest;
import com.loan.dto.CustomerResponse;
import com.loan.entity.Customer;
import com.loan.entity.UserRole;
import com.loan.entity.UserAccount;
import com.loan.repository.CustomerRepository;
import com.loan.repository.RoleRepository;
import com.loan.service.CustomerService;
import com.loan.service.UserService;
import com.loan.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final UserService userService;
    private final ValidationUtil validationUtil;
    private final RoleRepository roleRepository;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CustomerResponse create(CustomerRequest request) {
        log.info("Create customer request: {}", request);
        validationUtil.validate(request);

        UserRole customerUserRole = roleRepository.findByName("Customer")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role 'Customer' not found"));


        UserAccount userAccount = UserAccount.builder()
                .id(UUID.randomUUID().toString())
                .username(request.getUsername())
                .password(request.getPassword())
                .userRole(customerUserRole)
                .build();
        userService.create(userAccount);
        Customer customer = Customer.builder()
                .id(UUID.randomUUID().toString())
                .name(request.getName())
                .income(request.getIncome())
                .creditScore(request.getCreditScore())
                .employmentStatus(request.getEmploymentStatus())
                .email(request.getEmail())
                .phone(request.getPhone())
                .address(request.getAddress())
                .userAccount(userAccount)
                .build();
        customerRepository.save(
                customer.getId(),
                customer.getName(),
                customer.getAddress(),
                customer.getPhone(),
                customer.getIncome(),
                customer.getCreditScore(),
                customer.getEmploymentStatus(),
                customer.getEmail(),
                customer.getUserAccount().getId()
        );

        return toCustomerResponse(customer);
    }

    @Transactional(readOnly = true)
    @Override
    public List<CustomerResponse> getAll() {
        return customerRepository.findAll()
                .stream()
                .map(this::toCustomerResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public Customer getById(String id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));
    }

    @Transactional(readOnly = true)
    @Override
    public CustomerResponse getOne(String id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));
        return toCustomerResponse(customer);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CustomerResponse update(String id, CustomerRequest request) {
        validationUtil.validate(request);

        Customer customer = getById(id);

        UserAccount userAccount = (UserAccount) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        UserRole userRole = userAccount.getUserRole();
        if (userRole == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Role not found");
        }

        if ("Customer".equals(userRole.getName()) && !userAccount.getId().equals(customer.getUserAccount().getId()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not allowed to update this customer");

        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());
        customer.setAddress(request.getAddress());
        customerRepository.save(
                customer.getId(),
                customer.getName(),
                customer.getAddress(),
                customer.getPhone(),
                customer.getIncome(),
                customer.getCreditScore(),
                customer.getEmploymentStatus(),
                customer.getEmail(),
                customer.getUserAccount().getId()
        );

        return toCustomerResponse(customer);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteById(String id) {
        Customer customer = getById(id);
        customerRepository.delete(customer);
    }

    private CustomerResponse toCustomerResponse(Customer customer) {
        return CustomerResponse.builder()
                .id(customer.getId())
                .name(customer.getName())
                .address(customer.getAddress())
                .income(customer.getIncome())
                .creditScore(customer.getCreditScore())
                .employmentStatus(customer.getEmploymentStatus())
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .userId(customer.getUserAccount().getId())
                .build();
    }
}
