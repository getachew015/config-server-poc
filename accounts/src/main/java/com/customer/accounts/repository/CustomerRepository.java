package com.customer.accounts.repository;

import com.customer.accounts.model.BankAccounts;
import com.customer.accounts.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface CustomerRepository extends JpaRepository<Customer, String> {

    @Query("SELECT CASE WHEN (count(cust) > 0) THEN true ELSE false END "
            + "FROM Customer cust WHERE cust.customerId = :customerId")
    boolean customerExistsByCustomerId(@Param("customerId") String customerId);

    @Query("select cust from Customer cust where cust.customerId = :customerId")
    Customer findCustomerByCustomerId(@Param("customerId") String customerId);

}
