package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Balance;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
//@PreAuthorize("isAuthenticated()")
@RequestMapping("/account")
public class AccountController {

    private AccountDao accountDao;
    private UserDao userDao;

    public AccountController(AccountDao accountDao, UserDao userDao) {
        this.accountDao = accountDao;
        this.userDao = userDao;
    }
    @RequestMapping(path = "/balance", method = RequestMethod.GET)
    public BigDecimal getBalance(Principal principal) {
        String username = principal.getName();
        BigDecimal balance = accountDao.getBalance(userDao.findIdByUsername(username));
        return balance;
    }

//    @RequestMapping(path = "/balance", method = RequestMethod.PUT)
//    public BigDecimal addToBalance(BigDecimal amountToAdd, Principal principal) {
//        String username = principal.getName();
//        BigDecimal balance = accountDao.addToAccountBalance(amountToAdd, userDao.findIdByUsername(username));
//        return balance;
//    }

    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public Account updateBalance (@Valid @RequestBody Account account, @PathVariable int id) {
        if (id != account.getAccountId()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account Not Found");
        }
        return accountDao.update(account, id);
    }
    @RequestMapping(path = "/listOfUsers", method = RequestMethod.GET)
    public List<Account> getListOfUsers(Principal principal) {
        String userName = principal.getName();
        int id = userDao.findIdByUsername(userName);
        List<Account> userList = accountDao.getListOfUsers();
        return accountDao.getListOfUsers();
    }
    @RequestMapping(path = "/subtract", method = RequestMethod.PUT)
    void subtractFromAccountBalance(int fromAccount, BigDecimal amountToSubtract) {
        accountDao.subtractFromAccountBalance(fromAccount, amountToSubtract);
    }
    @RequestMapping(path = "/add", method = RequestMethod.PUT)
    void addToAccountBalance(int toAccount, BigDecimal amountToAdd) {
        accountDao.addToAccountBalance(toAccount, amountToAdd);
    }


//@RequestMapping(path = "balance/{id}", method = RequestMethod.PUT)
//public BigDecimal addToBalance (@Valid @RequestBody @PathVariable BigDecimal amountToAdd, @PathVariable int id) {
//    return accountDao.addToBalance(amountToAdd, id);
//}

}