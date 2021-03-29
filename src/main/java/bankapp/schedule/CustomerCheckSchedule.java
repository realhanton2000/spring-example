package bankapp.schedule;

import bankapp.factory.CustomProperties;
import bankapp.factory.RepoFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CustomerCheckSchedule {

    private static final Logger log = LoggerFactory.getLogger(CustomerCheckSchedule.class);

    @Autowired
    private CustomProperties customProperties;

    @Scheduled(fixedRate = 10000)
    public void countCustomer() {
        log.info("The number of Customer is " + RepoFactory.getInvokeStrategy(customProperties.getDbAccessType()).getCustomerRepo().count());
    }

    @Scheduled(fixedRate = 10000)
    public void countAccount() {
        log.info("The number of Account is " + RepoFactory.getInvokeStrategy(customProperties.getDbAccessType()).getAccountRepo().count());
    }
}
