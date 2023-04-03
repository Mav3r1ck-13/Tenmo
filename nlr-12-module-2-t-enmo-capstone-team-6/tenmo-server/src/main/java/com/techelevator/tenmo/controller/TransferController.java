package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.xml.crypto.dsig.TransformService;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/transfer")

public class TransferController {


    private TransferDao transferDao;
    private AccountDao accountDao;
    private UserDao userDao;

    public TransferController(TransferDao transferDao, UserDao userDao) {
        this.transferDao = transferDao;
        this.userDao = userDao;
    }
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public Transfer getTransferByTransferId(@PathVariable int id) {
        Transfer balance = transferDao.getTransferByTransferId(id);
        return balance;
    }
    @RequestMapping(path = "/list", method = RequestMethod.GET)
    public List<Transfer> listAllTransfers(Principal principal){
        String username = principal.getName();
        int userId = userDao.findIdByUsername(username);
        return transferDao.listAllTransfers(userId);
    }
    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public Transfer updateTransfer(@Valid @RequestBody Transfer transfer, @PathVariable int id) {
        return transferDao.updateTransfer(transfer, id);
    }
    @RequestMapping(path = "", method = RequestMethod.POST)
    public Transfer createTransfer(@RequestBody Transfer newTransfer) {
        transferDao.createTransfer(newTransfer);

        return newTransfer;
    }


}