package com.example.demo.mapper;

import com.example.demo.dto.Account;
import com.example.demo.dto.Balance;
import com.example.demo.util.ConvertionUtils;

import java.util.UUID;

public class Mapper {

    private Mapper() {
    }

    public static Account buildAccountResponse(com.example.demo.account.Account account) {
        return Account.builder()
                .id(account.getId())
                .name(account.getName())
                .createdBy(account.getCreatedBy())
                .createdDate(ConvertionUtils.fromGoogleTimestampUTC(account.getCreatedDate()))
                .lastModifiedBy(account.getLastModifiedBy())
                .lastModifiedDate(ConvertionUtils.fromGoogleTimestampUTC(account.getLastModifiedDate()))
                .build();
    }

    public static Balance buildBalanceResponse(com.example.demo.balance.Balance balance) {
        return Balance.builder()
                .id(UUID.fromString(balance.getId()))
                .accountId(balance.getAccountId())
                .balance(balance.getBalance())
                .createdBy(balance.getCreatedBy())
                .createdDate(ConvertionUtils.fromGoogleTimestampUTC(balance.getCreatedDate()))
                .build();
    }
}
