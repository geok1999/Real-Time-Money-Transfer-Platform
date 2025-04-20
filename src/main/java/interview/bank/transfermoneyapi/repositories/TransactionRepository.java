package interview.bank.transfermoneyapi.repositories;


import interview.bank.transfermoneyapi.domain.entities.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<TransactionEntity,Long> {

}
