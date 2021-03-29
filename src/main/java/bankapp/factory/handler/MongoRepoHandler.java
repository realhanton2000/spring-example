package bankapp.factory.handler;

import bankapp.factory.CustomProperties;
import bankapp.factory.RepoFactory;
import bankapp.model.Account;
import bankapp.model.Customer;
import bankapp.mongorepository.AccountMongoDBRepository;
import bankapp.mongorepository.CustomerMongoDBRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class MongoRepoHandler implements DataRepoHandler, InitializingBean {
    @Autowired
    private CustomerMongoDBRepository customerRepository;
    @Autowired
    private AccountMongoDBRepository accountRepository;

    @Override
    public CrudRepository<Customer, Long> getCustomerRepo() {
        if (customerRepository == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Application configuration problem - DB Access Type");
        }
        return customerRepository;
    }

    @Override
    public CrudRepository<Account, Long> getAccountRepo() {
        if (accountRepository == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Application configuration problem - DB Access Type");
        }
        return accountRepository;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        RepoFactory.register(CustomProperties.DBAccessType.MONGO, this);
    }
}
