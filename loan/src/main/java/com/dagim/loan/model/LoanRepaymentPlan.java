package com.dagim.loan.model;

import java.time.OffsetDateTime;
import javax.persistence.Column;
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
public class LoanRepaymentPlan {

  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  @Column(nullable = false, unique = true)
  private String loanRepaymentPlanId;
  private String simulationId;
  private OffsetDateTime loanDueDate;
  private double loanAmount;

}
