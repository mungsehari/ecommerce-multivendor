package com.hari.controller;

import com.hari.model.Seller;
import com.hari.model.Transaction;
import com.hari.service.SellerService;
import com.hari.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transactions")
public class TransactionController {
    private final TransactionService transactionService;
    private final SellerService sellerService;

    @GetMapping("/seller")
    public ResponseEntity<List<Transaction>> getTransactionBySeller(
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        Seller seller=sellerService.getSellerProfile(jwt);
        List<Transaction> transactions=transactionService.getTransactionBySellerId(seller);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        List<Transaction> transactions=transactionService.getAllTransactions();
        return ResponseEntity.ok(transactions);
    }
}
