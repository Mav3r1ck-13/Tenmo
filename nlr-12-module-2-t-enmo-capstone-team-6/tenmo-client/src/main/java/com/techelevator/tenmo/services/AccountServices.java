package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.User;
import com.techelevator.util.BasicLogger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;


public class AccountServices {
    private static final String API_BASE_URL = "http://localhost:8080/";
    private final RestTemplate restTemplate = new RestTemplate();

    public void getCurrentBalance() {

    }

    private Account getCurrentBalance(Account account) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Account> entity = new HttpEntity<>(account, headers);
        Account returnedBalance = null;
        try {
            returnedBalance = restTemplate.postForObject(API_BASE_URL, entity, Account.class);
        } catch (RestClientResponseException ex) {

            BasicLogger.log(ex.getRawStatusCode() + " : " + ex.getStatusText());
        }
        return returnedBalance;

    }
}
