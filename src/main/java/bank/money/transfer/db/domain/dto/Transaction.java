package bank.money.transfer.db.domain.dto;

import bank.money.transfer.util.Currency;
import jakarta.validation.constraints.*;
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
    @Positive(message = "Source account ID must be positive")
    private Long sourceAccountId;
    @NotNull
    @Positive(message = "Target account ID must be positive")
    private Long targetAccountId;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be at least 0.01")
    @DecimalMax(value = "999999.99", message = "Amount cannot exceed 999,999.99")
    @Digits(integer = 6, fraction = 2, message = "Amount must have at most 6 digits before decimal and 2 after")
    private BigDecimal amount;
    private Currency currency;
    private LocalDateTime createdAt;
}
