package com.example.demo.service;

import com.example.demo.balance.BalanceServiceGrpc;
import com.example.demo.balance.ReactorBalanceServiceGrpc;
import com.example.demo.dto.Balance;
import com.example.demo.dto.CreateBalanceRequest;
import com.example.demo.mapper.Mapper;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class BalanceService {

    @GrpcClient("balance-service")
    private ReactorBalanceServiceGrpc.ReactorBalanceServiceStub synchronousBalanceServiceClient;

    @GrpcClient("balance-service")
    private BalanceServiceGrpc.BalanceServiceStub asynchronousBalanceServiceClient;

    public Mono<Balance> createBalance(CreateBalanceRequest request) {
        Mono<com.example.demo.balance.Balance> b = synchronousBalanceServiceClient.createBalance(com.example.demo.balance.CreateBalanceRequest.newBuilder()
                .setAccountId(request.getAccountId())
                .setBalance(request.getBalance())
                .build());
        return b.map(Mapper::buildBalanceResponse);
    }
}
