package com.example.asm_java6_api.dto.response.discount;

import java.math.BigDecimal;
import java.time.Instant;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DiscountResponse {
    private BigDecimal percentage;
    private Instant startDate;
    private Instant endDate;
}
