package com.example.contributoryloanapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ContributoryLoanAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(ContributoryLoanAppApplication.class, args);
    }

}
