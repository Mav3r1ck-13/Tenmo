package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.AccountServices;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.ConsoleService;
import com.techelevator.tenmo.services.TransferService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";
    private AuthenticatedUser currentUser;
    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private final TransferService transferService = new TransferService(API_BASE_URL, currentUser);
    private RestTemplate restTemplate;

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }
    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account in all lower case.");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }


    private void viewCurrentBalance() {

        System.out.println(transferService.getBalance(currentUser.getToken()));
    }

    private void viewTransferHistory() {
        List<Transfer> transfers = transferService.listAllTransfers(currentUser.getToken());
        consoleService.printTransfers(transfers);


    }

    private void viewPendingRequests() {
        // TODO Auto-generated method stub

    }

    private void sendBucks() {
        List<Account> usersList = transferService.listOfUsers(currentUser.getToken());
        consoleService.printUsers(usersList);

        int selection = consoleService.promptForInt("Enter transfer ID to view details (0 to cancel): ");
        if (selection == 0) {
            mainMenu();
        }else if (selection == currentUser.getUser().getId()){
            System.out.println("ERROR Unable to transfer money to the same account.");
            mainMenu();
        }
        BigDecimal transferAmount = consoleService.promptForBigDecimal("Enter a decimal amount to send: ");
        Transfer transfer = new Transfer();
        transfer.setAccountFrom(currentUser.getUser().getId());
        transfer.setAccountTo(selection);
        transfer.setAmount(transferAmount);
        System.out.println(transfer.getAmount());
        transferService.subtractFromAccountBalance(currentUser.getUser().getId(), transferAmount);
        transferService.addToAccountBalance(selection, transferAmount);
        transferService.createTransfer(transfer, currentUser.getToken());
        System.out.println(transfer.getAccountTo());
        System.out.println(transfer.getAccountFrom());
        System.out.println("$" + transferAmount + " sent!");
        mainMenu();


    }



    private void requestBucks() {
        // TODO Auto-generated method stub

    }


}
