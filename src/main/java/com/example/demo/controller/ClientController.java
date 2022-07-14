package com.example.demo.controller;

import com.example.demo.dto.Account;
import com.example.demo.dto.Balance;
import com.example.demo.dto.CreateAccountRequest;
import com.example.demo.dto.CreateBalanceRequest;
import com.example.demo.dto.UpdateAccountRequest;
import com.example.demo.service.AccountService;
import com.example.demo.service.BalanceService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@AllArgsConstructor
public class ClientController {

    final AccountService accountService;
    final BalanceService balanceService;

    // curl --location --request GET 'http://localhost:8080/api/stream/accounts'
    @GetMapping(value = "/api/stream/accounts", produces = MediaType.TEXT_EVENT_STREAM_VALUE) // APPLICATION_NDJSON_VALUE
    public Flux<Account> streamAll() {
        log.info("Streaming accounts");
        return accountService.streamAll();
    }

    @GetMapping("/api/accounts/{name}")
    public ResponseEntity<Mono<Account>> getOneByName(@PathVariable String name) {
        return ResponseEntity.ok(accountService.getOneByName(name));
    }

    @GetMapping("/api/accounts")
    public ResponseEntity<Flux<Account>> getAll() {
        return ResponseEntity.ok(accountService.getAll());
    }

    @PostMapping("/api/accounts")
    public ResponseEntity<Mono<Account>> createAccount(@RequestBody CreateAccountRequest request) {
        return ResponseEntity.ok(accountService.createAccount(request.getName()));
    }

    @PutMapping("/api/accounts/{id}")
    public ResponseEntity<Mono<Account>> updateAccount(@PathVariable Long id, @RequestBody UpdateAccountRequest request) {
        return ResponseEntity.ok(accountService.updateAccount(id, request.getName()));
    }

    @PostMapping("/api/balances")
    public ResponseEntity<Mono<Balance>> createBalance(@RequestBody CreateBalanceRequest request) {
        return ResponseEntity.ok(balanceService.createBalance(request));
    }
}