package com.example.demo.service;

import com.example.demo.account.*;
import com.example.demo.dto.Account;
import com.example.demo.mapper.Mapper;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class AccountService {

    @GrpcClient("account-service")
    private ReactorAccountServiceGrpc.ReactorAccountServiceStub synchronousAccountServiceClient;

    @GrpcClient("account-service")
    private AccountServiceGrpc.AccountServiceStub asynchronousAccountServiceClient;

    public Mono<Account> getOneByName(String name) {
        Mono<com.example.demo.account.Account> a = synchronousAccountServiceClient.getOneByName(GetOneByNameRequest.newBuilder()
                .setName(name)
                .build());
        return a.map(Mapper::buildAccountResponse);
    }

    public Flux<Account> getAll() {
        Flux<com.example.demo.account.Account> accounts = synchronousAccountServiceClient.getAll(GetAllRequest.newBuilder().build());
        return accounts.map(Mapper::buildAccountResponse);
    }

    public Mono<Account> createAccount(String name) {
        Mono<com.example.demo.account.Account> a = synchronousAccountServiceClient.createAccount(CreateAccountRequest.newBuilder()
                .setName(name)
                .build());
        return a.map(Mapper::buildAccountResponse);
    }

    public Mono<Account> updateAccount(Long id, String name) {
        Mono<com.example.demo.account.Account> a = synchronousAccountServiceClient.updateAccount(UpdateAccountRequest.newBuilder()
                .setId(id)
                .setName(name)
                .build());
        return a.map(Mapper::buildAccountResponse);
    }
}
