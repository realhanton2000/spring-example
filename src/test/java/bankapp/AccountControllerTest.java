package bankapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AccountControllerTest {

    private static final ObjectMapper om = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    /*
customer
{
    "firstName": "joe",
    "lastName": "doe",
    "address": "dublin",
    "phoneNumber": "012222222",
    "ssn": "AAA112233"
}
account
{
    "accountType": "SAVINGS",
    "amount": "100.22",
    "customer": {
        "id": 1
    }
}
    */
    @Test
    public void save_read_200() throws Exception {
        String customerJson = "{\"firstName\": \"joe\",\"lastName\": \"doe\",\"address\":\"dublin\",\"phoneNumber\": \"012222222\",\"ssn\": \"AAA112233\"}";
        mockMvc.perform(put("/customer/1")
                .content(customerJson)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        String accountJson = "{\"accountType\": \"SAVINGS\",\"amount\": \"100.22\",\"customer\": {\"id\": 1}}";
        mockMvc.perform(put("/account/1")
                .content(accountJson)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(get("/account/1")
                .content(accountJson)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountType", is("SAVINGS")))
                .andExpect(jsonPath("$.amount", is(100.22)));
    }

    /*
customer
{
    "firstName": "joe",
    "lastName": "doe",
    "address": "dublin",
    "phoneNumber": "012222222",
    "ssn": "AAA112233"
}
account
{
    "accountType": "SAVINGS",
    "amount": "100.22",
    "customer": {
        "id": 1
    }
}
*/
    @Test
    public void save_delete_read_404() throws Exception {
        String customerJson = "{\"firstName\": \"joe\",\"lastName\": \"doe\",\"address\":\"dublin\",\"phoneNumber\": \"012222222\",\"ssn\": \"AAA112233\"}";
        mockMvc.perform(put("/customer/1")
                .content(customerJson)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        String accountJson = "{\"accountType\": \"SAVINGS\",\"amount\": \"100.22\",\"customer\": {\"id\": 1}}";
        mockMvc.perform(put("/account/1")
                .content(accountJson)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(delete("/account/1")
                .content(accountJson)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(get("/account/1")
                .content(accountJson)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(status().reason("Account doesn't exist."));
    }

    /*
customer
{
    "firstName": "joe",
    "lastName": "doe",
    "address": "dublin",
    "phoneNumber": "012222222",
    "ssn": "AAA112233"
}
account
{
    "accountType": "SAVINGS",
    "amount": "100.22",
    "customer": {
        "id": 1
    }
}
{
    "accountType": "CHECKING",
    "amount": "10.22",
    "customer": {
        "id": 1
    }
}
transfer
{
    "customerId": "1",
    "fromAccountId": "1",
    "toAccountId": "2",
    "amount": "20"
}
*/
    @Test
    public void transfer_200() throws Exception {
        String customerJson = "{\"firstName\": \"joe\",\"lastName\": \"doe\",\"address\":\"dublin\",\"phoneNumber\": \"012222222\",\"ssn\": \"AAA112233\"}";
        mockMvc.perform(put("/customer/1")
                .content(customerJson)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        String accountJson = "{\"accountType\": \"SAVINGS\",\"amount\": \"100.22\",\"customer\": {\"id\": 1}}";
        mockMvc.perform(put("/account/1")
                .content(accountJson)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        accountJson = "{\"accountType\": \"CHECKING\",\"amount\": \"10.22\",\"customer\": {\"id\": 1}}";
        mockMvc.perform(put("/account/2")
                .content(accountJson)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        String transferJson = "{\"customerId\": \"1\",\"fromAccountId\": \"1\",\"toAccountId\": \"2\",\"amount\": \"20\"}";
        mockMvc.perform(post("/transfer")
                .content(transferJson)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].amount", is(80.22)))
                .andExpect(jsonPath("$.[1].amount", is(30.22)));
    }

    /*
customer
{
"firstName": "joe",
"lastName": "doe",
"address": "dublin",
"phoneNumber": "012222222",
"ssn": "AAA112233"
}
account
{
"accountType": "SAVINGS",
"amount": "100.22",
"customer": {
    "id": 1
}
}
{
"accountType": "CHECKING",
"amount": "10.22",
"customer": {
    "id": 1
}
}
transfer
{
"customerId": "1",
"fromAccountId": "1",
"toAccountId": "2",
"amount": "200"
}
*/
    @Test
    public void transfer_400() throws Exception {
        String customerJson = "{\"firstName\": \"joe\",\"lastName\": \"doe\",\"address\":\"dublin\",\"phoneNumber\": \"012222222\",\"ssn\": \"AAA112233\"}";
        mockMvc.perform(put("/customer/1")
                .content(customerJson)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        String accountJson = "{\"accountType\": \"SAVINGS\",\"amount\": \"100.22\",\"customer\": {\"id\": 1}}";
        mockMvc.perform(put("/account/1")
                .content(accountJson)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        accountJson = "{\"accountType\": \"CHECKING\",\"amount\": \"10.22\",\"customer\": {\"id\": 1}}";
        mockMvc.perform(put("/account/2")
                .content(accountJson)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        String transferJson = "{\"customerId\": \"1\",\"fromAccountId\": \"1\",\"toAccountId\": \"2\",\"amount\": \"200\"}";
        mockMvc.perform(post("/transfer")
                .content(transferJson)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(status().reason("From Account doesn't have enough fund."));
    }

    /*
customer
{
"firstName": "joe",
"lastName": "doe",
"address": "dublin",
"phoneNumber": "012222222",
"ssn": "AAA112233"
}
{
"firstName": "john",
"lastName": "doe",
"address": "dublin",
"phoneNumber": "012222222",
"ssn": "AAA112233"
}
account
{
"accountType": "SAVINGS",
"amount": "100.22",
"customer": {
    "id": 1
}
}
{
"accountType": "CHECKING",
"amount": "10.22",
"customer": {
    "id": 1
}
}
transfer
{
"customerId": "1",
"fromAccountId": "1",
"toAccountId": "2",
"amount": "200"
}
*/
    @Test
    public void transfer2_400() throws Exception {
        String customerJson = "{\"firstName\": \"joe\",\"lastName\": \"doe\",\"address\":\"dublin\",\"phoneNumber\": \"012222222\",\"ssn\": \"AAA112233\"}";
        mockMvc.perform(put("/customer/1")
                .content(customerJson)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        customerJson = "{\"firstName\": \"john\",\"lastName\": \"doe\",\"address\":\"dublin\",\"phoneNumber\": \"012222222\",\"ssn\": \"AAA112233\"}";
        mockMvc.perform(put("/customer/2")
                .content(customerJson)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        String accountJson = "{\"accountType\": \"SAVINGS\",\"amount\": \"100.22\",\"customer\": {\"id\": 1}}";
        mockMvc.perform(put("/account/1")
                .content(accountJson)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        accountJson = "{\"accountType\": \"CHECKING\",\"amount\": \"10.22\",\"customer\": {\"id\": 2}}";
        mockMvc.perform(put("/account/2")
                .content(accountJson)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        String transferJson = "{\"customerId\": \"1\",\"fromAccountId\": \"1\",\"toAccountId\": \"2\",\"amount\": \"20\"}";
        mockMvc.perform(post("/transfer")
                .content(transferJson)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(status().reason("Account transfer failed. Account transfer is only allowed under the same Customer."));
    }
}