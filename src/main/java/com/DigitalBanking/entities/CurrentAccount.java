package com.DigitalBanking.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@DiscriminatorValue("CA")
@Data
public class CurrentAccount extends BankAccount {
    private double overDraft;
}