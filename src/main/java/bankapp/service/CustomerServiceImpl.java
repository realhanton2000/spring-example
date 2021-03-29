package bankapp.service;

import bankapp.factory.CustomProperties;
import bankapp.factory.RepoFactory;
import bankapp.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomProperties customProperties;

    @Override
    @CachePut(value = "numofcust")
    public long customerNumber(int index) {
        CrudRepository<Customer, Long> customerRepository = RepoFactory.getInvokeStrategy(customProperties.getDbAccessType()).getCustomerRepo();
        return customerRepository.count();
    }

    @Override
    @Cacheable(value = "numofcust")
    public long customerNumber2(int index) {
        return 100;
    }

}
