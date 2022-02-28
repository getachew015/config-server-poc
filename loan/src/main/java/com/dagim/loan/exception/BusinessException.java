package com.dagim.loan.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FAILED_DEPENDENCY)
public class BusinessException extends RuntimeException{

    public BusinessException(String errorMessage){
        super(errorMessage);
    }

}
