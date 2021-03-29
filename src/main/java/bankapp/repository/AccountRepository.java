package bankapp.repository;

import bankapp.model.Account;
import org.springframework.data.repository.CrudRepository;

/**
 * Account data repository
 */
public interface AccountRepository extends CrudRepository<Account, Long> {
}
