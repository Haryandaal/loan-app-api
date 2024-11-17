package com.loan.repository;

import com.loan.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface LoanRepository extends JpaRepository<Loan, String> {

    @Query(value = "SELECT * FROM loan WHERE approval_status = ?", nativeQuery = true)
    List<Loan> findByApprovalStatus(String status);

    @Query(value = "SELECT * FROM loan WHERE customer_id = ?", nativeQuery = true)
    List<Loan> findByCustomerId(String customerId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE loan SET approval_status = :status WHERE id = :loanId", nativeQuery = true)
    void updateApprovalStatus(String loanId, String status);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO loan (id, customer_id, amount, duration_in_months, interest_rate, approval_status, application_date) " +
            "VALUES (:id, :customerId, :amount, :duration, :interestRate, :approvalStatus, :applicationDate)", nativeQuery = true)
    void saveLoan(String id, String customerId, Long amount, Integer duration, Double interestRate, String approvalStatus, LocalDateTime applicationDate);

    @Query(value = "SELECT COUNT(*) FROM loan WHERE customer_id = :customerId AND approval_status = 'ACTIVE, PENDING, APPROVED'", nativeQuery = true)
    Integer countActiveLoansByCustomerId(String customerId);

    @Query(value = "SELECT COUNT(l) FROM Loan l JOIN Customer c ON l.customer.id = c.id WHERE c.id = :customerId AND l.approvalStatus = 'DEFAULTED'")
    int countDefaultedLoansByCustomerId(@Param("customerId") String customerId);

    Optional<Loan> findLoanById(String id);
}
