package com.techelevator.tenmo.services;


import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TransferService {

    private static final String API_BASE_URL = "http://localhost:8080/";
    private final String baseUrl;
    private final RestTemplate restTemplate = new RestTemplate();
    private AuthenticatedUser currentUser;

    public TransferService(String url, AuthenticatedUser currentUser) {
        this.baseUrl = url;
        this.currentUser = currentUser;
    }

    public BigDecimal getBalance(String userToken) {
        BigDecimal userBalance =  null;
        try {
            ResponseEntity<BigDecimal> response = restTemplate.exchange(API_BASE_URL + "account/balance", HttpMethod.GET, makeAuthEntity(userToken), BigDecimal.class);
            userBalance = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return userBalance;
    }
    public List<Transfer> listAllTransfers(String userToken) {
        Transfer[] transfers = null;
        try {
            ResponseEntity<Transfer[]> response = restTemplate.exchange(API_BASE_URL + "transfer/list", HttpMethod.GET, makeAuthEntity(userToken), Transfer[].class);
            transfers = response.getBody();

        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return Arrays.asList(transfers);
    }

    public List<Account> listOfUsers(String userToken) {
        Account[] usersList = null;
        try {
            ResponseEntity<Account[]> response = restTemplate.exchange(API_BASE_URL + "account/listOfUsers", HttpMethod.GET, makeAuthEntity(userToken), Account[].class);
            usersList = response.getBody();

        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return Arrays.asList(usersList);

    }

    public Transfer getTransfer(String userToken) {
        Transfer transfer = null;
        try {
            ResponseEntity<Transfer> response = restTemplate.exchange(API_BASE_URL + "transfer", HttpMethod.GET, makeAuthEntity(userToken), Transfer.class);
            transfer = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transfer;
    }

    public Account updatedBalances(Account updatedAccount){
        HttpEntity<Account> entity = makeAccountEntity(updatedAccount);
        try {
            restTemplate.put(API_BASE_URL + updatedAccount.getAccountId(), entity);
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return updatedAccount;
    }

    public User getUserByUserId(int id) {
        User user = null;
        try {
            user = restTemplate.getForObject(API_BASE_URL + "account/users" + id, User.class);
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return user;
    }

    public void createTransfer(Transfer transfer, String userToken) {
        try {
            ResponseEntity<Transfer> response = restTemplate.exchange(API_BASE_URL + "transfer", HttpMethod.POST, makeAuthEntity(userToken), Transfer.class);
            transfer = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
    }

    public boolean subtractFromAccountBalance(int accId, BigDecimal amountToSubtract) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Integer> entity = new HttpEntity<>(accId, headers);
            boolean success = false;
            try {
                restTemplate.put(API_BASE_URL + "account/subtract?fromAccount=" + accId + "&amountToSubtract=" + amountToSubtract, entity);
                success = true;
            } catch (RestClientResponseException | ResourceAccessException e) {
                BasicLogger.log(e.getMessage());
            }
            return success;
        }

    public boolean addToAccountBalance(int accId, BigDecimal amountToAdd) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Integer> entity = new HttpEntity<>(accId, headers);
        boolean success = false;
        try {
            restTemplate.put(API_BASE_URL + "account/add?toAccount=" + accId + "&amountToAdd=" + amountToAdd, entity);
            success = true;
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return success;
    }

    private HttpEntity<Account> makeAccountEntity(Account transfers) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String token = null;
        headers.setBearerAuth(token);
        return new HttpEntity<>(transfers, headers);
    }

    private HttpEntity<Void> makeAuthEntity(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return new HttpEntity<>(headers);
    }

}
