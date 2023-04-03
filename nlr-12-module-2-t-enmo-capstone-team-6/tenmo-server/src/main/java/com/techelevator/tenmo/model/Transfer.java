package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfer {

    private int transferId;
    private int transferTypeId;
    private int transferStatusId;
    private int accountFrom;
    private int accountTo;
    private BigDecimal amount = new BigDecimal("0");
    private String otherUser;
    private boolean wasSentToUs;
//    public Transfer(int transferId, int transferTypeId, int transferStatusId, int accountFrom, int accountInt, BigDecimal amount) {
//        this.transferId = transferId;
//        this.transferTypeId = transferTypeId;
//        this.transferStatusId = transferStatusId;
//        this.accountFrom = accountFrom;
//        this.accountInt = accountInt;
//        this.amount = amount;
//    }

    public int getTransferId() {
        return transferId;
    }

    public String getOtherUser() {
        return otherUser;
    }

    public void setOtherUser(String otherUser) {
        this.otherUser = otherUser;
    }

    public boolean isWasSentToUs() {
        return wasSentToUs;
    }

    public void setWasSentToUs(boolean wasSentToUs) {
        this.wasSentToUs = wasSentToUs;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public int getTransferTypeId() {
        return transferTypeId;
    }

    public void setTransferTypeId(int transferTypeId) {
        this.transferTypeId = transferTypeId;
    }

    public int getTransferStatusId() {
        return transferStatusId;
    }

    public void setTransferStatusId(int transferStatusId) {
        this.transferStatusId = transferStatusId;
    }

    public int getAccountFrom() {
        return accountFrom;
    }

    public int getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(int accountTo) {
        this.accountTo = accountTo;
    }

    public void setAccountFrom(int accountFrom) {
        this.accountFrom = accountFrom;
    }



    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
