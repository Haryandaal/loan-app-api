package com.loan.repository;

import com.loan.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {

    @Query(value = "SELECT * FROM customer", nativeQuery = true)
    List<Customer> findAll();

    @Query(value = "SELECT * FROM customer WHERE id = ?", nativeQuery = true)
    Optional<Customer> findById(String id);

//    Customer saveAndFlush(Customer customer);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO customer (id, name, address, phone_number, income, credit_score, employment_status, email, user_account_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
            nativeQuery = true)
    void save(String id,
                  String name,
                  String email,
                  String address,
                  Double income,
                  Double creditScore,
                  String employmentStatus,
                  String phone,
                  String userAccountId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM customer WHERE id = :#{#customer.id}", nativeQuery = true)
    void delete(@Param("customer") Customer customer);

    @Query("SELECT c.income FROM Customer c WHERE c.id = :customerId")
    double findCustomerIncomeById(@Param("customerId") String customerId);

    @Query("SELECT c.creditScore FROM Customer c WHERE c.id = :customerId")
    double findCustomerCreditScoreById(@Param("customerId") String customerId);
}
