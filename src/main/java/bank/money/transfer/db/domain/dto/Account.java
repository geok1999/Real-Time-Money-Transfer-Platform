package bank.money.transfer.db.domain.dto;


import bank.money.transfer.util.Currency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Account {

    private Long id;
    private BigDecimal balance;
    private Currency currency;
    private LocalDateTime createdAt;
}
