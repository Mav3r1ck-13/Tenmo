package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
@Component
public interface TransferDao {

    Transfer addToTransferTable(Transfer newTransfer);
    Transfer updateTransfer(Transfer transfer, int id);
    List<Transfer> listAllTransfers(int id);
    void createTransfer(Transfer transfer);
    List<Transfer> listPendingTransfers(int userId);
    Transfer getTransferByTransferId(int transferId);
}
