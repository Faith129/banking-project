package com.orbit.services.transactions;

import com.orbit.dto.response.ServiceResponse;

public interface BalanceService {
    ServiceResponse balanceEnquiry(String accountNo);
}
