package com.example.demo.service;

import com.example.demo.account.AccountServiceGrpc;
import com.example.demo.account.CreateAccountRequest;
import com.example.demo.account.GetAllRequest;
import com.example.demo.account.GetOneByNameRequest;
import com.example.demo.account.ReactorAccountServiceGrpc;
import com.example.demo.account.UpdateAccountRequest;
import com.example.demo.dto.Account;
import com.example.demo.mapper.Mapper;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import javax.annotation.PostConstruct;
import java.util.Comparator;

@Slf4j
@Service
public class AccountService {

    @GrpcClient("account-service")
    private ReactorAccountServiceGrpc.ReactorAccountServiceStub synchronousAccountServiceClient;

    @GrpcClient("account-service")
    private AccountServiceGrpc.AccountServiceStub asynchronousAccountServiceClient;

    // http://www.stackstalk.com/2022/01/server-sent-events-with-spring-webflux.html
    // https://reflectoring.io/getting-started-with-spring-webflux/
//    private final Sinks.Many<Account> accountsSink = Sinks.many().replay().latest();
     private final Sinks.Many<Account> accountsSink = Sinks.many().multicast().directBestEffort(); // onBackpressureBuffer()

    // Init the stream of accounts with existing accounts
//    @PostConstruct
//    public void init() {
//        this.getAll().map(account -> {
//            var result = accountsSink.tryEmitNext(account);
//            if (result.isFailure()) {
//                // do something here, since emission failed
//                log.error("Sink event emitter failure: {}", result);
//            }
//            return result;
//        }).subscribe();
//    }

    public Mono<Account> getOneByName(String name) {
        return synchronousAccountServiceClient.getOneByName(GetOneByNameRequest.newBuilder()
                        .setName(name)
                        .build())
                .map(Mapper::buildAccountResponse);
    }

    // TODO takeUntil https://stackoverflow.com/questions/47933765/terminate-particular-flux-stream-on-the-server
    public Flux<Account> streamAll() {
        return accountsSink.asFlux();
    }

    public Flux<Account> getAll() {
        return synchronousAccountServiceClient.getAll(GetAllRequest.newBuilder().build())
                .map(Mapper::buildAccountResponse)
                .sort(Comparator.comparing(Account::getId));
    }

    public Mono<Account> createAccount(String name) {
        CreateAccountRequest request = CreateAccountRequest.newBuilder()
                .setName(name)
                .build();
        return synchronousAccountServiceClient.createAccount(request)
                .map(Mapper::buildAccountResponse)
                .doOnSuccess(account -> {
                    var result = accountsSink.tryEmitNext(account);
                    if (result.isFailure()) {
                        // do something here, since emission failed
                        log.error("Sink event emitter failure: {}", result);
                    }
                });
    }

    public Mono<Account> updateAccount(Long id, String name) {
        UpdateAccountRequest request = UpdateAccountRequest.newBuilder()
                .setId(id)
                .setName(name)
                .build();
        return synchronousAccountServiceClient.updateAccount(request)
                .map(Mapper::buildAccountResponse)
                .doOnSuccess(account -> {
                    var result = accountsSink.tryEmitNext(account);
                    if (result.isFailure()) {
                        // do something here, since emission failed
                        log.error("Sink event emitter failure: {}", result);
                    }
                });
    }
}
