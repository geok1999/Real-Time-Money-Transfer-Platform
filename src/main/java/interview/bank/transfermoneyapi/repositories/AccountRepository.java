package interview.bank.transfermoneyapi.repositories;

import interview.bank.transfermoneyapi.domain.entities.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity,Long> {

}
