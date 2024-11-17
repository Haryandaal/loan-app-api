package com.loan.service.impl;

import com.loan.dto.LoanRequest;
import com.loan.dto.LoanResponse;
import com.loan.entity.ApprovalStatus;
import com.loan.entity.Customer;
import com.loan.entity.Loan;
import com.loan.entity.UserAccount;
import com.loan.repository.CustomerRepository;
import com.loan.repository.LoanRepository;
import com.loan.repository.UserAccountRepository;
import com.loan.service.LoanService;
import com.loan.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;
    private final CustomerRepository customerRepository;
    private final UserAccountRepository userAccountRepository;

    @Override
    public LoanResponse applyForLoan(String customerId, LoanRequest request) {
        int activeLoans = loanRepository.countActiveLoansByCustomerId(customerId);
        if (activeLoans > 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Customer cannot apply for a new loan while having active loans.");
        }

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));

        // Validasi: Periksa status pekerjaan
        if ("Unemployed".equals(customer.getEmploymentStatus())) {
            throw new IllegalStateException("Customer is unemployed and cannot apply for a loan.");
        }


        Loan loan = Loan.builder()
                .id(UUID.randomUUID().toString())
                .customer(customer)
                .amount(request.getAmount())
                .duration(request.getDuration())
                .interestRate(request.getInterestRate())
                .approvalStatus(ApprovalStatus.PENDING)
                .applicationDate(LocalDateTime.now())
                .build();

        loanRepository.saveLoan(
                loan.getId(),
                loan.getCustomer().getId(),
                loan.getAmount(),
                loan.getDuration(),
                loan.getInterestRate(),
                loan.getApprovalStatus().getDescription(),
                loan.getApplicationDate()
        );

        return toLoanResponse(loan);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public LoanResponse evaluateLoan(String loanId, String userId, boolean isApproved, String rejectionReason) {

        UserAccount admin = userAccountRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Admin not found"));
        if (!"Admin".equals(admin.getUserRole().getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only admins can approve or reject loans.");
        }

        Loan loan = loanRepository.findLoanById(loanId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Loan not found"));

        // Validate Risk
        double creditScore = customerRepository.findCustomerCreditScoreById(loan.getCustomer().getId());
        double income = customerRepository.findCustomerIncomeById(loan.getCustomer().getId());
        int defaultedLoans = loanRepository.countDefaultedLoansByCustomerId(loan.getCustomer().getId());

        if (creditScore < 500)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Loan cannot be approved due to low credit score.");


        double monthlyInstallment = (double) loan.getAmount() / loan.getDuration();
        if (monthlyInstallment > income * 0.3)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Loan cannot be approved due to insufficient income.");

        if (defaultedLoans > 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Loan cannot be approved due to poor repayment history.");

        // Approval or Rejection Process
        if (isApproved) {
            loanRepository.updateApprovalStatus(loanId, ApprovalStatus.APPROVED.getDescription());
            loan.setApprovalStatus(ApprovalStatus.APPROVED);
        } else {
            if (rejectionReason == null || rejectionReason.trim().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rejection reason must be provided.");
            }
            loanRepository.updateApprovalStatus(loanId, ApprovalStatus.REJECTED.getDescription());
            loan.setApprovalStatus(ApprovalStatus.REJECTED);
            loan.setRejectionReason(rejectionReason);
        }

        return toLoanResponse(loan);

    }

    private LoanResponse toLoanResponse(Loan loan) {

        return LoanResponse.builder()
                .id(loan.getId())
                .customerId(loan.getCustomer().getId())
                .amount(loan.getAmount())
                .duration(loan.getDuration())
                .interestRate(loan.getInterestRate())
                .approvalStatus(loan.getApprovalStatus().getDescription())
                .rejectionReason(loan.getRejectionReason())
                .applicationDate(DateUtil.localDateTimeToString(loan.getApplicationDate()))
                .build();
    }


}
