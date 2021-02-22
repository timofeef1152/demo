package com.example.demo.model.dto;

import com.example.demo.model.enums.Currency;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountPaymentDto {
    @NonNull
    private String fromAccNumber;
    @NonNull
    private String toAccNumber;
    @NonNull
    private Currency currency;
    @NonNull
    private Long amount;
}
