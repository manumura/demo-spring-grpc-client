package com.example.demo.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
public class Balance {

    private UUID id;
    private Long accountId;
    private Long balance;
    private String createdBy;
    private LocalDateTime createdDate;
}
