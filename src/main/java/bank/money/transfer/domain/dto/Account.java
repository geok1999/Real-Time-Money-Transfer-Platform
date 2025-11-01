package bank.money.transfer.domain.dto;


import bank.money.transfer.util.Currency;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Version;


import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Account {

    @NotNull(message = "Account ID is required")
    @Positive(message = "Account ID must be positive")
    private Long id;

    @NotNull(message = "Balance is required")
    @DecimalMin(value = "0.00", message = "Balance cannot be negative")
    @Digits(integer = 10, fraction = 2, message = "Balance format is invalid")
    private BigDecimal balance;

    private Currency currency;

    @PastOrPresent(message = "Created date cannot be in the future")
    private LocalDateTime createdAt;

    @Version
    private Long version;
}
