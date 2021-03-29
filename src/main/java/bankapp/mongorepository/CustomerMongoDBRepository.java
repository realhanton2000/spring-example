package bankapp.mongorepository;

import bankapp.model.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Customer data repository
 */
public interface CustomerMongoDBRepository extends MongoRepository<Customer, Long> {
}
