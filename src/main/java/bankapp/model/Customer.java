package bankapp.model;

import bankapp.view.Views;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Data model for customer. It is used as DB data model and json data model.
 */
@Entity
@Table(name = "customer")
@Document(collection = "customer")
//@JsonIgnoreProperties(value = {"ssn"}, allowSetters = true)
public class Customer implements Serializable {

    /**
     * customer id
     */
    @JsonView(Views.All.class)
    @Id
    private long id;
    /**
     * customer first name
     */
    @JsonView(Views.All.class)
    @NotEmpty(message = "First name is required")
    private String firstName;
    /**
     * customer last name
     */
    @JsonView(Views.All.class)
    @NotEmpty(message = "Last name is required")
    private String lastName;
    /**
     * customer address
     */
    @JsonView(Views.All.class)
    @NotEmpty(message = "Address is required")
    private String address;
    /**
     * customer phone number
     */
    @JsonView(Views.All.class)
    @NotEmpty(message = "Phone number is required")
    private String phoneNumber;
    /**
     * customer social security number
     */
    @JsonView(Views.Internal.class)
    @NotEmpty(message = "SSN is required")
    private String ssn;

    /**
     * foreign ref to account
     */
    @JsonIgnore
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Account> accounts;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    @Override
    public String toString() {
        List<String> info = new ArrayList<>();
        info.add(Long.toString(getId()));
        info.add(getFirstName());
        info.add(getLastName());
        info.add(getSsn());
        return info.stream().reduce((a, b) -> (a + ":" + b)).orElse(Long.toString(getId()));
    }
}
