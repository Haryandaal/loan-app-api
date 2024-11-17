package com.loan.controller;

import com.loan.dto.LoanRequest;
import com.loan.dto.LoanResponse;
import com.loan.dto.RoleResponse;
import com.loan.dto.WebResponse;
import com.loan.service.LoanService;
import com.loan.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;


    @PostMapping(path = "apply")
    public ResponseEntity<WebResponse<LoanResponse>> applyLoan(@RequestParam String customerId , @RequestBody LoanRequest request) {
        LoanResponse response = loanService.applyForLoan(customerId, request);
        return ResponseUtil.buildResponse(HttpStatus.CREATED, "successfully apply for loan", response);
    }

    @PutMapping("/{loanId}/evaluate")
    public ResponseEntity<WebResponse<LoanResponse>> evaluateLoan(
            @PathVariable String loanId,
            @RequestParam String adminId,
            @RequestParam boolean isApproved,
            @RequestBody (required = false) String rejectionReason) {

        LoanResponse loanResponseDTO = loanService.evaluateLoan(loanId, adminId, isApproved, rejectionReason);
        return ResponseUtil.buildResponse(HttpStatus.OK, "successfully evaluate loan", loanResponseDTO);
    }
    
}
