package bank.money.transfer.db.domain.entities;


import bank.money.transfer.util.Currency;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "account")
public class AccountEntity {

    @Id
    private Long id;

    //@NotNull
    //@PositiveOrZero
    @Column(precision = 19, scale = 4)
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
