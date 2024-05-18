package com.DigitalBanking.dtos;

import lombok.Data;
import java.util.Date;
@Data
public class SavingBankAccountDTO extends BankAccountDTO {
    private String id;
    private double balance;
    private Date createdAt;
    private CustomerDTO customerDTO;
    private double interestRate;
}
