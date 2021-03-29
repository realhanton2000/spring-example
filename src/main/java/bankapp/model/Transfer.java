package bankapp.model;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Json data model for accounts transfer
 */
public class Transfer {

    /**
     * the customer id
     */
    @NotNull(message = "Customer ID is required")
    @Min(value = 1, message = "Customer ID is required")
    private long customerId;

    /**
     * the from account id
     */
    @NotNull(message = "From Account ID is required")
    @Min(value = 1, message = "From Account ID is required")
    private long fromAccountId;

    /**
     * the to account id
     */
    @NotNull(message = "To Account ID is required")
    @Min(value = 1, message = "To Account ID is required")
    private long toAccountId;

    /**
     * the credit transfer amount
     */
    @NotNull(message = "Amount is required")
    @Digits(integer = 10, fraction = 2, message = "max 10 integral digits and 2 fractional digits are allowed")
    private double amount;

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public long getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(long fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public long getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(long toAccountId) {
        this.toAccountId = toAccountId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
