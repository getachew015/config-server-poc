package com.dagim.loan.repository;

import com.dagim.loan.model.SimulatedLoan;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface LoanSimulationRepository extends JpaRepository<SimulatedLoan, String> {

}
