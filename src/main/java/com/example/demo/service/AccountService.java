package com.example.demo.service;

import com.example.demo.account.AccountServiceGrpc;
import com.example.demo.account.CreateAccountRequest;
import com.example.demo.account.GetAllRequest;
import com.example.demo.account.GetOneByNameRequest;
import com.example.demo.dto.Account;
import com.example.demo.mapper.Mapper;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class AccountService {

    @GrpcClient("account-service")
    private AccountServiceGrpc.AccountServiceBlockingStub synchronousAccountServiceClient;

    @GrpcClient("account-service")
    private AccountServiceGrpc.AccountServiceStub asynchronousAccountServiceClient;

    public Account getOneByName(String name) {
        com.example.demo.account.Account account = synchronousAccountServiceClient.getOneByName(GetOneByNameRequest.newBuilder()
                .setName(name)
                .build());
        return Mapper.buildAccountResponse(account);
    }

    public List<Account> getAll() throws InterruptedException {
        final CountDownLatch finishLatch = new CountDownLatch(1);
        final List<Account> accounts = new ArrayList<>();
        asynchronousAccountServiceClient.getAll(GetAllRequest.newBuilder().build(),
                new StreamObserver<>() {
                    @Override
                    public void onNext(com.example.demo.account.Account account) {
                        log.info("Next: {}", account);
                        accounts.add(Mapper.buildAccountResponse(account));
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Status status = Status.fromThrowable(throwable);
                        log.warn("Error: {}", status);
                        finishLatch.countDown();
                    }

                    @Override
                    public void onCompleted() {
                        log.info("Completed");
                        finishLatch.countDown();
                    }
                });

        // Receiving happens asynchronously
        boolean await = finishLatch.await(1, TimeUnit.MINUTES);
        return await ? accounts : Collections.emptyList();
    }

    public Account createAccount(String name) {
        com.example.demo.account.Account account = synchronousAccountServiceClient.createAccount(CreateAccountRequest.newBuilder()
                .setName(name)
                .build());
        return Mapper.buildAccountResponse(account);
    }
}
