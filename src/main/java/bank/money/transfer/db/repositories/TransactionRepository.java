package bank.money.transfer.db.repositories;


import bank.money.transfer.db.domain.entities.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<TransactionEntity,Long> {

}
