package bankapp.controller;

import bankapp.Application;
import bankapp.factory.CustomProperties;
import bankapp.factory.RepoFactory;
import bankapp.message.JmsProducer;
import bankapp.model.Customer;
import bankapp.service.CustomerService;
import bankapp.view.Views;
import com.fasterxml.jackson.annotation.JsonView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.Optional;

/**
 * Controller for Customer Management
 */
@RestController
@Validated
public class CustomerController {

    Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    private CustomProperties customProperties;

    @Autowired
    JmsProducer jmsProducer;

    /**
     * Save or update a customer
     *
     * @param newCustomer the customer data
     * @param id          the customer id
     * @return the saved or updated customer data
     */
    @JsonView(Views.External.class)
    @PutMapping("/customer/{id}")
    public Customer persistCustomer(@Valid @RequestBody Customer newCustomer, @PathVariable @Min(1) long id) {
        CrudRepository<Customer, Long> customerRepository = RepoFactory.getInvokeStrategy(customProperties.getDbAccessType()).getCustomerRepo();

        return customerRepository.findById(id)
                .map(customer -> {
                    customer.setFirstName(newCustomer.getFirstName());
                    customer.setLastName(newCustomer.getLastName());
                    customer.setAddress(newCustomer.getAddress());
                    customer.setPhoneNumber(newCustomer.getPhoneNumber());
                    customer.setSsn(newCustomer.getSsn());
                    jmsProducer.sendMessage(customer);
                    return customerRepository.save(customer);
                }).orElseGet(() -> {
                    newCustomer.setId(id);
                    jmsProducer.sendMessage(newCustomer);
                    return customerRepository.save(newCustomer);
                });
    }

    /**
     * Retrieve a customer
     *
     * @param id the customer id
     * @return the customer data
     */
    @JsonView(Views.External.class)
    @GetMapping("/customer/{id}")
    public Customer getCustomer(@PathVariable @Min(1) long id) {
        CrudRepository<Customer, Long> customerRepository = RepoFactory.getInvokeStrategy(customProperties.getDbAccessType()).getCustomerRepo();

        return customerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account doesn't exist."));
    }

    @Autowired
    CustomerService customerService;

    @GetMapping("/customerNum")
    public long customerNum() {
        return customerService.customerNumber(1);
    }

    @GetMapping("/customerNum2")
    public long customerNum2() {
        return customerService.customerNumber2(1);
    }

    @GetMapping("/printBeans")
    public void printBeans() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(Application.class);
        String[] definitionNames = ac.getBeanDefinitionNames();
        for (String name : definitionNames) {
            logger.info("bean - " + name);
        }
    }

    /**
     * Delete a customer
     *
     * @param id the customer id
     */
    @DeleteMapping("/customer/{id}")
    public void deleteCustomer(@PathVariable @Min(1) long id) {
        CrudRepository<Customer, Long> customerRepository = RepoFactory.getInvokeStrategy(customProperties.getDbAccessType()).getCustomerRepo();

        Optional<Customer> customerOptional = customerRepository.findById(id);
        if (customerOptional.isPresent()) {
            customerRepository.delete(customerOptional.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account doesn't exist.");
        }
    }
}
