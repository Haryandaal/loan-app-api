package com.loan.service.impl;

import com.loan.entity.Loan;
import com.loan.repository.LoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreditScoreServiceImpl {

    private LoanRepository loanRepository;

    public double calculateCreditScore(String customerId) {
        int activeLoans = loanRepository.countActiveLoansByCustomerId(customerId);
        int defaultedLoans = loanRepository.countDefaultedLoansByCustomerId(customerId);

        // Start with default score
        double score = 800;

        // Penalti untuk pinjaman aktif
        if (activeLoans > 3) {
            score -= 50;
        }

        // Penalti untuk pinjaman gagal bayar
        score -= defaultedLoans * 100;

        // Skor minimal
        return Math.max(score, 300); // Skor minimal 300
    }
}
