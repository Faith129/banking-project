package com.orbit.services.transactions;

import com.orbit.dto.request.AccountRequest;
import com.orbit.dto.request.SignupRequest;
import com.orbit.dto.response.AccountResponse;
import com.orbit.dto.response.ServiceResponse;
import com.orbit.exceptions.ServiceException;
import com.orbit.models.Account;
import com.orbit.models.auth.Users;
import com.orbit.repository.AccountRepository;
import com.orbit.utils.Applicationutils;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.orbit.enums.ResponseCode.*;

@Slf4j
@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository acctRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ServiceResponse createAccount(AccountRequest accountDto) {

        ServiceResponse response;

        if (acctRepo.existsByAccountNo(accountDto.getAccountNo())) {
            throw new ServiceException(Integer.valueOf(ALREADY_EXIST.getCanonicalCode()),
                "Account with Account Number '" + accountDto.getAccountNo() + "' already exists",
                LocalDateTime.now().toString());
        }

        if (accountDto.getOpeningBalance() == null
            || accountDto.getOpeningBalance().compareTo(new BigDecimal("1000.00")) < 0) {
            throw new ServiceException(Integer.valueOf(BAD_REQUEST.getCanonicalCode()),
                "Minimum opening deposit amount is #1000", LocalDateTime.now().toString());
        }

        Account account = Account.builder().accountName(accountDto.getAccountName())
            .openingBalance(accountDto.getOpeningBalance())
            .accountNo(alwaysGetUniqueAccountNumber()).dateOpened(new Date()).transactions(new ArrayList<>())
            .totalBalance(accountDto.getOpeningBalance()).build();

        try {
            response = new ServiceResponse(OK.getCanonicalCode(), OK.getDescription(), LocalDateTime.now().toString(),
                acctRepo.save(account));
        } catch (Exception e) {
            log.error("Exception occurred while creating account {}", e.getMessage());
            return new ServiceResponse(INTERNAL_SERVER_ERROR.getCanonicalCode(), INTERNAL_SERVER_ERROR.getDescription(),
                LocalDateTime.now().toString(), null);
        }

        return response;


    }
@Override
    public ServiceResponse getAccountInfo(String accountNo) {
        return acctRepo.findByAccountNo(accountNo).map(account -> {
            AccountResponse response = AccountResponse.builder().accountName(account.getAccountName())
                .accountNo(account.getAccountNo()).totalBalance(account.getTotalBalance()).build();
            return new ServiceResponse(OK.getCanonicalCode(), OK.getDescription(), LocalDateTime.now().toString(),
                response);
        }).orElseThrow(() -> new ServiceException(Integer.valueOf(NOT_FOUND.getCanonicalCode()),
            NOT_FOUND.getDescription(), LocalDateTime.now().toString()));
    }

    private String alwaysGetUniqueAccountNumber() {
        String accountNo = "";
        for (; ; ) {
            accountNo = Applicationutils.generateUniqueAccountNumber();
            if (!acctRepo.existsByAccountNo(accountNo)) {
                break;
            }
        }
        return accountNo;
    }

    @Override
    public ServiceResponse update(String accountNo, AccountRequest accountDto) {
        ServiceResponse response;
        Optional<Account> account = acctRepo.findByAccountNo(accountNo);
        if (account.isPresent()) {
            Account accountUpdate = account.get();
            accountUpdate.setAccountNo(accountDto.getAccountNo());
            accountUpdate.setAccount_type(accountDto.getAccount_type());
            accountUpdate.setAccountName(accountDto.getAccountName());
            accountUpdate.setTotalBalance(accountDto.getTotalBalance());
            accountUpdate.setTransactions(accountDto.getTransactions());
acctRepo.save(accountUpdate);
        }
        try {
            response = new ServiceResponse(OK.getCanonicalCode(), OK.getDescription(), LocalDateTime.now().toString()
                );
        } catch (Exception e) {
            log.error("Exception occurred while creating account {}", e.getMessage());
            return new ServiceResponse(INTERNAL_SERVER_ERROR.getCanonicalCode(), INTERNAL_SERVER_ERROR.getDescription(),
                LocalDateTime.now().toString(), null);
        }

        return response;

    }
}
