package com.dagim.loan.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class BusinessException extends ResponseStatusException {

    public BusinessException(HttpStatus httpStatus, String errorMessage ){
        super(httpStatus, errorMessage);
    }

}
