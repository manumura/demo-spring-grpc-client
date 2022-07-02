package com.example.demo.service;

import com.example.demo.balance.BalanceServiceGrpc;
import com.example.demo.dto.Balance;
import com.example.demo.dto.CreateBalanceRequest;
import com.example.demo.mapper.Mapper;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BalanceService {

    @GrpcClient("balance-service")
    private BalanceServiceGrpc.BalanceServiceBlockingStub synchronousBalanceServiceClient;

    @GrpcClient("balance-service")
    private BalanceServiceGrpc.BalanceServiceStub asynchronousBalanceServiceClient;

    public Balance createBalance(CreateBalanceRequest request) {
        com.example.demo.balance.Balance b = synchronousBalanceServiceClient.createBalance(com.example.demo.balance.CreateBalanceRequest.newBuilder()
                .setAccountId(request.getAccountId())
                .setBalance(request.getBalance())
                .build());
        return Mapper.buildBalanceResponse(b);
    }
}
