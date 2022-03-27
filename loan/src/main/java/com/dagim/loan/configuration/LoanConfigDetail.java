package com.dagim.loan.configuration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "loan.paylater")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanConfigDetail {

    private int frequency;
    private int maxAmount;
}
