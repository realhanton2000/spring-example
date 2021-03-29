package bankapp.controller;

import bankapp.factory.CustomProperties;
import bankapp.factory.RepoFactory;
import bankapp.model.Account;
import bankapp.model.Customer;
import bankapp.model.Transfer;
import bankapp.view.Views;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Controller for Account Management
 */
@RestController
@Validated
public class AccountController {

    @Autowired
    private CustomProperties customProperties;

    /**
     * Save or update an Account
     *
     * @param newAccount the account data
     * @param id         the Account id
     * @return the saved or updated account data
     */
    @JsonView(Views.External.class)
    @PutMapping("/account/{id}")
    public Account persistAccount(@Valid @RequestBody Account newAccount, @PathVariable @Min(1) long id) {
        CrudRepository<Customer, Long> customerRepository = RepoFactory.getInvokeStrategy(customProperties.getDbAccessType()).getCustomerRepo();

        Optional<Customer> customerOptional = customerRepository.findById(newAccount.getCustomer().getId());
        if (customerOptional.isPresent()) {
            CrudRepository<Account, Long> accountRepository = RepoFactory.getInvokeStrategy(customProperties.getDbAccessType()).getAccountRepo();

            return accountRepository.findById(id)
                    .map(account -> {
                        account.setAccountType(newAccount.getAccountType());
                        account.setAmount(newAccount.getAmount());
                        account.setCustomer(customerOptional.get());
                        return accountRepository.save(account);
                    }).orElseGet(() -> {
                        newAccount.setId(id);
                        newAccount.setCustomer(customerOptional.get());
                        return accountRepository.save(newAccount);
                    });
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer doesn't exist.");
        }
    }

    /**
     * Retrieve an account information
     *
     * @param id the account id
     * @return the account data
     */
    @JsonView(Views.External.class)
    @GetMapping("/account/{id}")
    public Account getAccount(@PathVariable @Min(1) long id) {
        CrudRepository<Account, Long> accountRepository = RepoFactory.getInvokeStrategy(customProperties.getDbAccessType()).getAccountRepo();

        return accountRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account doesn't exist."));
    }

    /**
     * Delete an account
     *
     * @param id the account id
     */
    @DeleteMapping("/account/{id}")
    public void deleteAccount(@PathVariable @Min(1) long id) {
        CrudRepository<Account, Long> accountRepository = RepoFactory.getInvokeStrategy(customProperties.getDbAccessType()).getAccountRepo();

        Optional<Account> accountOptional = accountRepository.findById(id);
        if (accountOptional.isPresent()) {
            accountRepository.deleteById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account doesn't exist.");
        }
    }

    /**
     * Round up double
     *
     * @param value double
     * @param scale scale
     * @return double
     */
    private double round(double value, int scale) {
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(scale, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    /**
     * Transfer credit between two accounts
     *
     * @param transfer the payload contains credit transfer information
     * @return the two accounts data after credit transfer
     */
    @JsonView(Views.External.class)
    @PostMapping("/transfer")
    public List<Account> transfer(@Valid @RequestBody Transfer transfer) {
        if (transfer.getFromAccountId() == transfer.getToAccountId()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "From and To Accounts cannot be same.");
        }

        CrudRepository<Customer, Long> customerRepository = RepoFactory.getInvokeStrategy(customProperties.getDbAccessType()).getCustomerRepo();

        Customer customer = customerRepository.findById(transfer.getCustomerId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Customer doesn't exist."));

        CrudRepository<Account, Long> accountRepository = RepoFactory.getInvokeStrategy(customProperties.getDbAccessType()).getAccountRepo();

        Account fromAccount = accountRepository.findById(transfer.getFromAccountId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "From Account doesn't exist."));
        if (fromAccount.getAmount() < transfer.getAmount()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "From Account doesn't have enough fund.");
        }
        Account toAccount = accountRepository.findById(transfer.getToAccountId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "To Account doesn't exist."));
        if (fromAccount.getCustomer() != toAccount.getCustomer()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account transfer failed. Account transfer is only allowed under the same Customer.");
        }

        fromAccount.setAmount(round(fromAccount.getAmount() - transfer.getAmount(), 2));
        toAccount.setAmount(round(toAccount.getAmount() + transfer.getAmount(), 2));

        Account fromAccountSaved = accountRepository.save(fromAccount);
        Account toAccountSaved = accountRepository.save(toAccount);
        List<Account> accounts = new ArrayList<>();
        accounts.add(fromAccountSaved);
        accounts.add(toAccountSaved);
        return accounts;
    }
}
