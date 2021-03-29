package bankapp.factory.handler;

import bankapp.model.Account;
import bankapp.model.Customer;
import org.springframework.data.repository.CrudRepository;

public interface DataRepoHandler {

    CrudRepository<Customer, Long> getCustomerRepo();

    CrudRepository<Account, Long> getAccountRepo();

}
