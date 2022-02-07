package com.customer.accounts.repository;

import com.customer.accounts.model.BankAccounts;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
@Transactional
public interface BankAccountsRepository extends JpaRepository<BankAccounts, String> {

  @Query("SELECT CASE WHEN (count(accts) > 0) THEN true ELSE false END "
      + "FROM BankAccounts accts WHERE accts.customerId = :customerId")
  boolean customerExistsByCustomerId(@Param("customerId") String customerId);

  @Query("select accts from BankAccounts accts where accts.customerId = :customerId")
  List<BankAccounts> findAccountsByCustomerId(@Param("customerId") String customerId);


}
