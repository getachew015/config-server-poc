package com.dagim.loan.model;

import java.time.OffsetDateTime;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonProperty;
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
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private String loanId;
  private OffsetDateTime loanDueDate;
  private double loanAmount;

}
