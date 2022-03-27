package com.dagim.loan.repository;

import com.dagim.loan.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import javax.transaction.Transactional;

@Transactional
public interface LoanSimulationRepository extends JpaRepository<Loan, String> {

}
