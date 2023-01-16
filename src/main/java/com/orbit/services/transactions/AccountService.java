package com.orbit.services.transactions;

import com.orbit.dto.request.AccountRequest;
import com.orbit.dto.response.ServiceResponse;

public interface AccountService {
    ServiceResponse createAccount(AccountRequest accountRequest);
    ServiceResponse update(String accountNo, AccountRequest accountDto);
    ServiceResponse getAccountInfo(String accountNo);
}
