package com.customer.accounts.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer implements Serializable {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String customerRefNumber;
    private String customerId;
    private String custFirstName;
    private String custLastName;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(referencedColumnName = "customerId", name = "customerId")
    private Set<BankAccounts> bankAccounts;
}
