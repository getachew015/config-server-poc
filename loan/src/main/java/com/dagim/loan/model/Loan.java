package com.dagim.loan.model;

import java.time.OffsetDateTime;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GeneratorType;
import org.hibernate.annotations.GenericGenerator;


@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Loan {

  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  @Column(nullable = false, unique = true)
  private String loanId;
  private String customerId;
  private OffsetDateTime loanRequestDate;
  private double loanAmount;
  private String installmentState;
  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "loanId", referencedColumnName = "loanId")
  private Set<LoanRepaymentPlan> repaymentPlan;

}
