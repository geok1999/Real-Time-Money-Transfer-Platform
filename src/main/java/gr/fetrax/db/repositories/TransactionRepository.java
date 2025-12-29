package gr.fetrax.db.repositories;


import gr.fetrax.db.domain.entities.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<TransactionEntity,Long> {

}
