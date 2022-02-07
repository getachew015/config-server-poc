package com.customer.accounts.model;

import java.time.OffsetDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankAccounts {

  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  private String accountNumber;
  private String customerId;
  private String accountType;
  private OffsetDateTime accountOpenDate;
  private double accountBalance;

}
