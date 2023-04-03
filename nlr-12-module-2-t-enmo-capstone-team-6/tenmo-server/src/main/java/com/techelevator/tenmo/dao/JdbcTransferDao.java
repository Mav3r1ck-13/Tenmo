package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
@Component
public class JdbcTransferDao implements TransferDao{
    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;
    }
    private JdbcTemplate jdbcTemplate;

    //Creates a new transfer
    @Override
    public void createTransfer(Transfer transfer) {
        String sql = "insert into transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                "VALUES (2, 2, ?, ?, 1000.00) RETURNING transfer_id;";
        jdbcTemplate.queryForObject(sql, Integer.class, transfer.getTransferTypeId(), transfer.getTransferStatusId(), transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount());
    }

    //Updates existing transfer
    @Override
    public Transfer updateTransfer(Transfer transfer, int id) {
        String sql = "UPDATE transfer " +
                "SET transfer_type_id = ?, transfer_status_id = ?, account_from = ?, account_to = ?, amount = ? " +
                "WHERE transfer_id = ?";

        jdbcTemplate.update(sql, transfer.getTransferTypeId(), transfer.getTransferStatusId(), transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount(), id);
        return transfer;
    }

    //Lists all transfers
    @Override
    public List<Transfer> listAllTransfers(int id) {
        List<Transfer> transferList = new ArrayList<>();
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount, account.user_id\n" +
                "FROM transfer\n" +
                "INNER JOIN account\n" +
                "ON account.account_id = transfer.account_from\n" +
                "WHERE user_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
        while(results.next()) {
            Transfer transfer = mapRowToTransfer(results);
            transferList.add(transfer);
        }
        return transferList;
    }

    //Gets a Transfer by ID and gives additional information
    @Override
    public Transfer getTransferByTransferId(int id) {
        Transfer transfer = null;
        String sql = "select transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount " +
                "from transfer where transfer_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql,id);
        if(results.next()) {
            transfer = mapRowToTransfer(results);
        }
        return transfer;
    }
    //Lists all pending transfers
    @Override
    public List<Transfer> listPendingTransfers(int userId) {
        List<Transfer> pendingList = new ArrayList<>();
        String sql = "SELECT transfer_id, transfer_type_id, transfer.transfer_status_id, account_from, account_to, amount " +
                "FROM transfer " +
                "JOIN account ON account.account_id = transfer.account_from " +
                "JOIN transfer_status ON transfer.transfer_status_id = transfer_status.transfer_status_id " +
                "WHERE transfer_status_desc = 'Pending'";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while(results.next()) {
            Transfer transfer = mapRowToTransfer(results);
            pendingList.add(transfer);
        }
        return pendingList;
    }
    public Transfer addToTransferTable(Transfer newTransfer) {
        String sqlPostToTransfer = "insert into transfers (transfer_type_id, transfer_status_id, account_from, account_to, amount)"
                + "values (2, 2, ?, ?, ?)";
        jdbcTemplate.update(sqlPostToTransfer, newTransfer.getTransferTypeId(), newTransfer.getAccountFrom(), newTransfer.getAccountTo(), newTransfer.getAmount());
        return newTransfer;

    }


    private Transfer mapRowToTransfer(SqlRowSet rs) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(rs.getInt("transfer_id"));
        transfer.setTransferTypeId(rs.getInt("transfer_type_id"));
        transfer.setTransferStatusId(rs.getInt("transfer_status_id"));
        transfer.setAccountFrom(rs.getInt("account_from"));
        transfer.setAccountTo(rs.getInt("account_to"));
        transfer.setAmount(rs.getBigDecimal("amount"));
        return transfer;
    }
}
