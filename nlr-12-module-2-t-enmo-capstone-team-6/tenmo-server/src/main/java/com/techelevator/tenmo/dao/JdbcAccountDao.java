package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Balance;
import org.springframework.boot.autoconfigure.quartz.QuartzProperties;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.w3c.dom.stylesheets.LinkStyle;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcAccountDao implements AccountDao {
    public JdbcAccountDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Account> getListOfUsers() {
        List<Account> userAccounts = new ArrayList<>();
        String sql = "SELECT account_id, user_id, balance\n" +
                "FROM account;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while(results.next()) {
            userAccounts.add(mapResultsToAccount(results));
        }
        return userAccounts;
    }


    @Override
    public BigDecimal getBalance(int userId) {
        BigDecimal balance = null;
        String sqlString = "SELECT balance FROM account WHERE user_id = ?";
        SqlRowSet results = null;
            results = jdbcTemplate.queryForRowSet(sqlString, userId);
            if (results.next()) {
                balance = results.getBigDecimal("balance");
            }
        return balance;
    }

    @Override
    public void addToAccountBalance(int toAccount, BigDecimal amountToAdd) {
        String sql = "UPDATE account " +
                "SET balance = balance + ? " +
                "WHERE user_id = ?";
        jdbcTemplate.update(sql, amountToAdd, toAccount);

    }
    @Override
    public void subtractFromAccountBalance(int fromAccount, BigDecimal amountToSubtract) {
        String sql = "UPDATE account " +
                "SET balance = balance - ? " +
                "WHERE user_id = ?";
        jdbcTemplate.update(sql, amountToSubtract, fromAccount);
    }
    @Override
    public Account update(Account account, int id) {
        List<Account> accounts = null;
        Account result = account;
        boolean finished = false;

        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).getAccountId() == id) {
                if( result.getAccountId() == 0 ) {
                    result.setAccountId(id);
                }
                accounts.set(i, result);
                finished = true;
                break;
            }
        }
        if (!finished) {
            return null;
        }

        return result;
    }

    @Override
    public Account getAccount() {
        return null;
    }

    @Override
    public Account getAccountByUserId(int userId) {
        String sql = "SELECT account_id, user_id, balance FROM account WHERE user_id = ?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, userId);
        Account account = null;
        if (result.next()) {
            account = mapResultsToAccount(result);
        }
        return account;
    }

    @Override
    public Account getAccountByAccountId(int accountId) {
        String sql = "SELECT account_id, user_id, balance FROM account WHERE account_id = ?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, accountId);
        Account account = null;
        if (result.next()) {
            account = mapResultsToAccount(result);
        }
        return account;
    }

    @Override
    public void updateAccount(Account accountToUpdate) {
        String sql = "UPDATE account " +
                "SET balance = ? " +
                "WHERE account_id = ?";

        jdbcTemplate.update(sql, accountToUpdate.getBalance(), accountToUpdate.getAccountId(), accountToUpdate.getUserId());
    }

    private Account mapResultsToAccount(SqlRowSet result) {
        Account account = new Account();
        account.setBalance(result.getBigDecimal("balance"));
        account.setAccountId(result.getInt("account_id"));
        account.setUserId(result.getInt("user_id"));
        return account;
    }
}