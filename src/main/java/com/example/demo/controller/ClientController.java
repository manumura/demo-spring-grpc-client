package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.service.AccountService;
import com.example.demo.service.BalanceService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
public class ClientController {

    final AccountService accountService;
    final BalanceService balanceService;

    @GetMapping("/api/accounts/{name}")
    public ResponseEntity<Account> getOneByName(@PathVariable String name) {
        return ResponseEntity.ok(accountService.getOneByName(name));
    }

    @GetMapping("/api/accounts")
    public ResponseEntity<List<Account>> getAll() {
        try {
            return ResponseEntity.ok(accountService.getAll());
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
            // Restore interrupted state...
            Thread.currentThread().interrupt();
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/api/accounts")
    public ResponseEntity<Account> createAccount(@RequestBody CreateAccountRequest request) {
        return ResponseEntity.ok(accountService.createAccount(request.getName()));
    }

    @PutMapping("/api/accounts/{id}")
    public ResponseEntity<Account> updateAccount(@PathVariable Long id, @RequestBody UpdateAccountRequest request) {
        return ResponseEntity.ok(accountService.updateAccount(id, request.getName()));
    }

    @PostMapping("/api/balances")
    public ResponseEntity<Balance> createBalance(@RequestBody CreateBalanceRequest request) {
        return ResponseEntity.ok(balanceService.createBalance(request));
    }
}