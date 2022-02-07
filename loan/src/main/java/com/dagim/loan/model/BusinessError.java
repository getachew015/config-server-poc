package com.dagim.loan.model;

public class BusinessError extends Exception{

  public BusinessError(String errorMessage){
    super(errorMessage);
  }

}
