package bankapp.repository;

import bankapp.model.Customer;
import org.springframework.data.repository.CrudRepository;

/**
 * Customer data repository
 */
public interface CustomerRepository extends CrudRepository<Customer, Long> {
}
