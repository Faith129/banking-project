package com.orbit.models;

import lombok.*;


import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "NIP_TXN_HISTORY")
public class Transactions {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true)
    private Long id;
    @Column(name = "TRAN_REF")
    private String transactionRef;
    @Column(name = "account_no")
    private String accountNo;
    @Column(name = "TRAN_APPL")
    private String tranAppl;
    @Column(name = "FROM_ACCT_NUM")
    private String fromAccountNumber;
    @Column(name = "To_ACCT_NUM")
    private String toAccountNumber;
    @Column(name = "TRAN_RECEIVER")
    private String transReceiver;
    @Column(name = "TRAN_AMOUNT")
    private BigDecimal transAmount;
    @Column(name = "TRAN_STATUS")
    private String tranStatus;
    @Column(name = "NARRATION")
    private String narration;
      @Column(name = "TRAN_TYPE")
    private String transactionType;
    @Column(name = "TRAN_DATE")
    private String transactionDate;
    private BigDecimal balanceEnquiries;

    public Transactions(String fromAccountNumber, BigDecimal amount, String toString) {
    }
}
