package com.example.demo.service;

import com.example.demo.model.dto.AccountPaymentDto;
import com.example.demo.model.entity.Account;
import com.example.demo.model.entity.AccountPayment;
import com.example.demo.model.entity.User;
import com.example.demo.repository.AccountPaymentRepository;
import com.example.demo.repository.AccountRepository;
import com.example.demo.security.UserPrincipal;
import com.example.demo.exceptions.AccountPermissionDeniedException;
import com.example.demo.exceptions.CurrencyDoesntMatchException;
import com.example.demo.exceptions.InsufficientFundsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountPaymentRepository accountPaymentRepository;

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void payment(AccountPaymentDto accountPaymentDto) throws AccountPermissionDeniedException,
            CurrencyDoesntMatchException, InsufficientFundsException {
        Account fromAcc = accountRepository.findByAccountNumber(accountPaymentDto.getFromAccNumber())
                .orElseThrow(() -> new EntityNotFoundException("Account " + accountPaymentDto.getFromAccNumber() + " doesn't exist!"));
        Account toAcc = accountRepository.findByAccountNumber(accountPaymentDto.getFromAccNumber())
                .orElseThrow(() -> new EntityNotFoundException("Account " + accountPaymentDto.getToAccNumber() + " doesn't exist!"));

        AccountPayment accountPayment = new AccountPayment();
        accountPayment.setFromAccount(fromAcc);
        accountPayment.setToAccount(toAcc);
        accountPayment.setCurrency(accountPaymentDto.getCurrency());
        accountPayment.setAmount(accountPaymentDto.getAmount());

        validateOperation(accountPayment);

        Long fromAccNewBalance = fromAcc.getBalance() - Math.abs(accountPaymentDto.getAmount());
        fromAcc.setBalance(fromAccNewBalance);
        Long toAccNewBalance = toAcc.getBalance() + Math.abs(accountPaymentDto.getAmount());
        toAcc.setBalance(toAccNewBalance);

        accountRepository.save(fromAcc);
        accountRepository.save(toAcc);
        accountPaymentRepository.save(accountPayment);
    }

    @Transactional
    public Account create(Account account) {
        return accountRepository.save(account);
    }

    private void validateOperation(AccountPayment accountPayment) throws AccountPermissionDeniedException,
            CurrencyDoesntMatchException, InsufficientFundsException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = ((UserPrincipal) principal).getUser();
        if (!accountPayment.getFromAccount().getUser().getId().equals(user.getId())) {
            throw new AccountPermissionDeniedException("account permission denied");
        }

        if (!accountPayment.getCurrency().equals(accountPayment.getFromAccount().getCurrency())
                || !accountPayment.getCurrency().equals(accountPayment.getToAccount().getCurrency())) {
            throw new CurrencyDoesntMatchException(String.format("currency: operation %s | from_account %s | to_account %s",
                    accountPayment.getCurrency(),
                    accountPayment.getFromAccount().getCurrency(),
                    accountPayment.getToAccount().getCurrency()));
        }

        if (accountPayment.getFromAccount().getBalance() < accountPayment.getAmount()) {
            throw new InsufficientFundsException("insufficient funds");
        }
    }
}
