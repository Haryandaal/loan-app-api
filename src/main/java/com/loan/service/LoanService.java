package com.loan.service;

import com.loan.dto.LoanRequest;
import com.loan.dto.LoanResponse;
import org.springframework.transaction.annotation.Transactional;

public interface LoanService {

    LoanResponse applyForLoan(String customerId, LoanRequest request);

    LoanResponse evaluateLoan(String loanId, String userId, boolean isApproved, String rejectionReason);
}
