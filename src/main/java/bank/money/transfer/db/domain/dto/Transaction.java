package bank.money.transfer.db.domain.dto;

import bank.money.transfer.util.Currency;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
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
public class Transaction {
    private Long id;
    @NotNull
    private Long sourceAccountId;
    @NotNull
    private Long targetAccountId;
    @NotNull
    @DecimalMin(value = "0.01", inclusive = true)
    private BigDecimal amount;
    private Currency currency;
    private LocalDateTime createdAt;
}
