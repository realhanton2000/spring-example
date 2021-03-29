package bankapp.service;

import bankapp.factory.CustomProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl2 implements CustomerService {

    @Autowired
    private CustomProperties customProperties;

    @Override
    public long customerNumber(int index) {
        return 2;
    }

    @Override
    public long customerNumber2(int index) {
        return 4;
    }

}
