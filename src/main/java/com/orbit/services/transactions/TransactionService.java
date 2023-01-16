package com.orbit.services.transactions;

import com.orbit.dto.request.TransactionRequest;
import com.orbit.dto.response.ServiceResponse;

import javax.security.auth.login.AccountNotFoundException;

public interface TransactionService {
    ServiceResponse withdraw(TransactionRequest withdrawalRequest);
    ServiceResponse deposit(TransactionRequest depositRequest);
    ServiceResponse amountTransfer(TransactionRequest transferRequest) throws AccountNotFoundException;
}
