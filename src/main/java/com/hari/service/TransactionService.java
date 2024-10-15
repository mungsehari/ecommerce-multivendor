package com.hari.service;

import com.hari.model.Order;
import com.hari.model.Seller;
import com.hari.model.Transaction;

import java.util.List;

public interface TransactionService {
    Transaction createTransaction(Order order);
    List<Transaction> getTransactionBySellerId(Seller seller);
    List<Transaction> getAllTransactions();
}
