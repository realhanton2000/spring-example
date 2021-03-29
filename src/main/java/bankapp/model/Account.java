package bankapp.model;

import bankapp.view.Views;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

/**
 * Data model for account. It is used as DB data model and json data model.
 */
@Entity
@Table(name = "account")
@Document(collection = "account")
public class Account {

    /**
     * Enum of account types
     */
    enum AccountType {
        CHECKING, SAVINGS, MONEYMARKET
    }

    /**
     * account id
     */
    @JsonView(Views.All.class)
    @Id
    private long id;

    /**
     * account type
     */
    @JsonView(Views.All.class)
    @NotNull(message = "Account type is required")
    private AccountType accountType;

    /**
     * account amount
     */
    @JsonView(Views.All.class)
    @NotNull(message = "Amount is required")
    @Digits(integer = 10, fraction = 2, message = "max 10 integral digits and 2 fractional digits are allowed")
    private double amount;

    /**
     * foreign ref to customer
     */
    @JsonView(Views.All.class)
    @ManyToOne
    @JoinColumn(name = "customerId", referencedColumnName = "id")
    @NotNull(message = "Customer ref is required")
    @DBRef
    private Customer customer;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
