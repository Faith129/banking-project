package com.orbit.services.transactions;

import com.orbit.dto.request.TransactionRequest;
import com.orbit.dto.response.ServiceResponse;
import com.orbit.exceptions.ServiceException;
import com.orbit.models.Account;
import com.orbit.models.Transactions;
import com.orbit.repository.AccountRepository;
import com.orbit.repository.TransactionRepository;
import com.orbit.utils.Applicationutils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

import static com.orbit.enums.ResponseCode.*;

@Service
public class TransactionServiceImpl implements  TransactionService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionRepository transRepo;
    final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public ServiceResponse withdraw(TransactionRequest withdrawalRequest) {
        return accountRepository.findByAccountNo(withdrawalRequest.getAccountNo()).map(withdraw -> {
            if (withdrawalRequest.getTransAmount() == null || withdrawalRequest.getTransAmount().compareTo(new BigDecimal("1000.00")) < 0) {
                throw new ServiceException(Integer.valueOf(BAD_REQUEST.getCanonicalCode()),
                    "Minimum allowed withdrawal #1000.00", LocalDateTime.now().toString());
            }

            if (withdraw.getTotalBalance().subtract(withdrawalRequest.getTransAmount()).compareTo(new BigDecimal("500.00")) < 0) {
                throw new ServiceException(Integer.valueOf(BAD_REQUEST.getCanonicalCode()),
                    "You cannot withdraw the specified amount as minimum account balance is #500",
                    LocalDateTime.now().toString());
            }

            withdraw.setTotalBalance(withdraw.getTotalBalance().subtract(withdrawalRequest.getTransAmount()));
            withdraw.getTransactions()
                .add(Transactions.builder().transAmount(withdrawalRequest.getTransAmount()).narration(withdrawalRequest.getNarration())
                    .transactionType("Debit").transactionDate(formatter.format(new Date()))
                    .balanceEnquiries(withdraw.getTotalBalance()).build());
            accountRepository.save(withdraw);
            return new ServiceResponse(OK.getCanonicalCode(), OK.getDescription(), LocalDateTime.now().toString(),
                Applicationutils.SUCCESSFULL_WITHDRAWAL);
        }).orElseThrow(() -> new ServiceException(Integer.valueOf(NOT_FOUND.getCanonicalCode()),
            NOT_FOUND.getDescription(), LocalDateTime.now().toString()));

    }

    @Override
    public ServiceResponse deposit(TransactionRequest depositRequest) {
        return accountRepository.findByAccountNo(depositRequest.getAccountNo()).map(transactions -> {
            if (depositRequest.getTransAmount().compareTo(new BigDecimal("50.00")) <= 0) {
                throw new ServiceException(Integer.valueOf(BAD_REQUEST.getCanonicalCode()),
                    "Minimum deposit amount is #50.00", LocalDateTime.now().toString());
            }

            transactions.setTotalBalance(transactions.getTotalBalance().add(depositRequest.getTransAmount()));
            transactions.getTransactions()
                .add(Transactions.builder().transAmount(depositRequest.getTransAmount()).narration(depositRequest.getNarration())
                    .transactionType("Credit").transactionDate(formatter.format(new Date()))
                    .balanceEnquiries(transactions.getTotalBalance()).build());
            accountRepository.save(transactions);
             return new ServiceResponse(OK.getCanonicalCode(), OK.getDescription(), LocalDateTime.now().toString(),
                Applicationutils.SUCCESSFULL_DEPOSIT);
        }).orElseThrow(() -> new ServiceException(Integer.valueOf(NOT_FOUND.getCanonicalCode()),
            NOT_FOUND.getDescription(), LocalDateTime.now().toString()));
    }
@Transactional
    @Override
    public ServiceResponse amountTransfer(TransactionRequest transferRequest) throws AccountNotFoundException {
        String fromAccountNumber = transferRequest.getFromAccountNumber();
        String toAccountNumber = transferRequest.getToAccountNumber();
        BigDecimal amount = transferRequest.getTransAmount();

        Account fromAccount = accountRepository.findByAccountNo(fromAccountNumber).orElseThrow(()->
            new AccountNotFoundException(" Account " + fromAccountNumber + " Not Found "));
        Account toAccount = accountRepository.findByAccountNo(toAccountNumber).orElseThrow(()->
           new AccountNotFoundException("Account" + toAccountNumber + " Does not found "));
        if(fromAccount.getTotalBalance().compareTo(BigDecimal.ONE) == 1
            && fromAccount.getTotalBalance().compareTo(amount) == 1
        ){
            fromAccount.setTotalBalance(fromAccount.getTotalBalance().subtract(amount));
            accountRepository.save(fromAccount);
            toAccount.setTotalBalance(toAccount.getTotalBalance().add(amount));
            accountRepository.save(toAccount);
//            Transactions transaction = transRepo.save(new Transactions(fromAccountNumber,amount, LocalDateTime.now().toString()));
//            return transaction;.compareTo(transfer.getAmount()) < 0
            if (fromAccount.getTotalBalance().compareTo(transferRequest.getTransAmount())>0) {
                throw new ServiceException(Integer.valueOf(BAD_REQUEST.getCanonicalCode()),
                    "Insufficient Fund",
                    LocalDateTime.now().toString());
            }

        }
    return new ServiceResponse(OK.getCanonicalCode(), OK.getDescription(), LocalDateTime.now().toString(),
        Applicationutils.SUCCESSFUL_TRANSFER);
    }

}
