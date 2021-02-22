package com.example.demo.preset;

import com.example.demo.model.dto.JwtRequest;
import com.example.demo.model.entity.Account;
import com.example.demo.model.entity.User;
import com.example.demo.model.enums.Currency;
import com.example.demo.service.AccountService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Component
public class SetupDefaultUser {
    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;

    @Transactional
    @PostConstruct
    protected void setup() {
        String username = "user";
        String password = "useruser";
        if (userService.existsWithUsername(username)) {
            return;
        }
        String accountNumber1 = "423015896354862591444";
        String accountNumber2 = "423015864325862591555";
        Long balance = 800L;
        Currency currency = Currency.USD_CENT;
        Account account1 = createAccount(accountNumber1, balance, currency);
        Account account2 = createAccount(accountNumber2, balance, currency);
        List<Account> accounts = new ArrayList<>();
        accounts.add(account1);
        accounts.add(account2);
        User user = createUser(username, password);
        User entity = userService.addAccounts(user, accounts);
    }

    private User createUser(String username, String password) {
        JwtRequest userDto = new JwtRequest(username, password);
        return userService.create(userDto);
    }

    private Account createAccount(String accountNumber, Long balance, Currency currency) {
        Account account = new Account();
        account.setAccountNumber(accountNumber);
        account.setBalance(balance);
        account.setCurrency(currency);
        return accountService.create(account);
    }
}