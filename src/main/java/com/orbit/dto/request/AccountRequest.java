package com.orbit.dto.request;

import com.orbit.models.Transactions;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountRequest {
    private String accountNo;
    private String accountName;
    private LocalDate dateOpened;
    private BigDecimal totalBalance;
    private LocalDate lastTranDate;
    private BigDecimal openingBalance;
    private String account_type;
    private List<Transactions> transactions = new ArrayList<>();
}
