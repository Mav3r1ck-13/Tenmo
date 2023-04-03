package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Balance;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public interface AccountDao {

    BigDecimal getBalance(int userId);
    void subtractFromAccountBalance(int fromAccount, BigDecimal amountToSubtract);
    Account update(Account account,int id);
            void addToAccountBalance(int toAccount, BigDecimal amountToAdd);
    Account getAccount();
    Account getAccountByUserId(int userId);
    Account getAccountByAccountId(int accountId);
    void updateAccount(Account accountToUpdate);
    List<Account> getListOfUsers();

}
