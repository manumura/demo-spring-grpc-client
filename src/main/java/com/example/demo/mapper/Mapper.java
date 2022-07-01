package com.example.demo.mapper;

import com.example.demo.dto.Account;
import com.example.demo.util.ConvertionUtils;

public class Mapper {

    private Mapper() {}

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

//    public static com.example.demo.balance.Balance buildBalanceResponse(Balance b) {
//        com.example.demo.balance.Balance.Builder builder = com.example.demo.balance.Balance.newBuilder();
//        builder.setId(b.getId().toString())
//                .setBalance(b.getBalance())
//                .setAccountId(b.getAccountId())
//                .setCreatedBy(b.getCreatedBy())
//                .setCreatedDate(ConvertionUtils.toGoogleTimestampUTC(b.getCreatedDate()));
//        return builder.build();
//    }
}
