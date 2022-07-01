package com.example.demo.controller;

import com.example.demo.dto.Account;
import com.example.demo.service.AccountService;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
public class ClientController {

    final AccountService accountService;

    @GetMapping("/api/accounts/{name}")
    public ResponseEntity<Account> getAuthor(@PathVariable String name) {
        try {
            return ResponseEntity.ok(accountService.getOneByName(name));
        } catch (StatusRuntimeException e) {
            log.error(e.getMessage(), e);
            if (Status.NOT_FOUND.equals(e.getStatus())) {
                return ResponseEntity.notFound().build();
            }
            if (Status.FAILED_PRECONDITION.equals(e.getStatus())) {
                return ResponseEntity.badRequest().build();
            }
            return ResponseEntity.internalServerError().build();
        }
    }
}