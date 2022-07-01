package com.example.demo.service;

import com.example.demo.account.AccountServiceGrpc;
import com.example.demo.account.GetOneByNameRequest;
import com.example.demo.dto.Account;
import com.example.demo.mapper.Mapper;
import com.example.demo.util.ConvertionUtils;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AccountService {

    @GrpcClient("account-service")
    private AccountServiceGrpc.AccountServiceBlockingStub synchronousAccountServiceClient;

    public Account getOneByName(String name) {
        com.example.demo.account.Account account = synchronousAccountServiceClient.getOneByName(GetOneByNameRequest.newBuilder()
                .setName(name)
                .build());
        return Mapper.buildAccountResponse(account);
    }
}
