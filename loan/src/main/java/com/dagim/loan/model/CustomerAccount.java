package com.dagim.loan.model;

import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerAccount {


  private String accountNumber;
  private String customerId;
  private String accountType;
  private OffsetDateTime accountOpenDate;


}
