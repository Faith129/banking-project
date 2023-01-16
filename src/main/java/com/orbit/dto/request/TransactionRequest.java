package com.orbit.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequest {
    private String transactionRef;
    private String accountNo;
    private String tranAppl;
    private String fromAccountNumber;
    private String toAccountNumber;
    private String transReceiver;
    private BigDecimal transAmount;
    private String tranStatus;
    private String narration;
    private String transactionType;
    private LocalDate transactionDate;
}
