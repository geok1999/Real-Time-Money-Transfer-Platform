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
public class Account {
    @NotNull
    private Long id;
    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal balance;
    private Currency currency;
    private LocalDateTime createdAt;
}
