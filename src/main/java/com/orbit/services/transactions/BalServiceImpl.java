package com.orbit.services.transactions;

import com.orbit.dto.response.ServiceResponse;
import com.orbit.exceptions.ServiceException;
import com.orbit.models.BalanceEnquiry;
import com.orbit.repository.AccountRepository;
import com.orbit.repository.BalanceRepository;
import com.orbit.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.orbit.enums.ResponseCode.NOT_FOUND;
import static com.orbit.enums.ResponseCode.OK;

@Service
public class BalServiceImpl implements BalanceService {

@Autowired
    TransactionRepository transRepo;

@Autowired
AccountRepository accountRepository;
    @Override
    public ServiceResponse balanceEnquiry(String accountNo) {
        return accountRepository.findByAccountNo(accountNo).map(balanceEnquiry -> {
            return new ServiceResponse(OK.getCanonicalCode(), OK.getDescription(), LocalDateTime.now().toString(),
                balanceEnquiry.getTotalBalance());
        }).orElseThrow(() -> new ServiceException(Integer.valueOf(NOT_FOUND.getCanonicalCode()),
            NOT_FOUND.getDescription(), LocalDateTime.now().toString()));
    }


}
