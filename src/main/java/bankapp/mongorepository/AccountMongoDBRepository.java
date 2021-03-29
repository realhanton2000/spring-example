package bankapp.mongorepository;

import bankapp.model.Account;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Account data repository
 */
public interface AccountMongoDBRepository extends MongoRepository<Account, Long> {
}
