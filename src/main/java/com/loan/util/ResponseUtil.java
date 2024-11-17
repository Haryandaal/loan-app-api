package com.loan.util;

import com.loan.dto.WebResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class ResponseUtil {

    public static <T> ResponseEntity<WebResponse<T>> buildResponse(HttpStatus httpStatus, String message, T data) {
        WebResponse<T> response = new WebResponse<>(httpStatus.value(), message, data);
        return ResponseEntity.status(httpStatus).body(response);
    }
}
