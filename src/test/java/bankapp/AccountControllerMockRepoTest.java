package bankapp;

import bankapp.model.Account;
import bankapp.repository.AccountRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.validation.ConstraintViolationException;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AccountControllerMockRepoTest {

    private static final ObjectMapper om = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountRepository mockRepository;

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
    public void save_successful_200() throws Exception {
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

        verify(mockRepository, times(1)).save(any(Account.class));
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
    "amount": "100.222",
    "customer": {
        "id": 1
    }
}
    */
    @Test
    public void save_wrongAmount_400() throws Exception {
        String customerJson = "{\"firstName\": \"joe\",\"lastName\": \"doe\",\"address\":\"dublin\",\"phoneNumber\": \"012222222\",\"ssn\": \"AAA112233\"}";
        mockMvc.perform(put("/customer/1")
                .content(customerJson)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        String accountJson = "{\"accountType\": \"SAVINGS\",\"amount\": \"100.222\",\"customer\": {\"id\": 1}}";
        mockMvc.perform(put("/account/1")
                .content(accountJson)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp", is(notNullValue())))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.message", is("max 10 integral digits and 2 fractional digits are allowed")));

        verify(mockRepository, times(0)).save(any(Account.class));
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
    "accountType": "OK",
    "amount": "100.22",
    "customer": {
        "id": 1
    }
}
    */
    @Test
    public void save_wrongAccountType_400() throws Exception {
        String customerJson = "{\"firstName\": \"joe\",\"lastName\": \"doe\",\"address\":\"dublin\",\"phoneNumber\": \"012222222\",\"ssn\": \"AAA112233\"}";
        mockMvc.perform(put("/customer/1")
                .content(customerJson)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        String accountJson = "{\"accountType\": \"OK\",\"amount\": \"100.22\",\"customer\": {\"id\": 1}}";
        mockMvc.perform(put("/account/1")
                .content(accountJson)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(mockRepository, times(0)).save(any(Account.class));
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
    "accountType": "CHECKING",
    "amount": "100.22",
    "customer": {
        "id": 1
    }
}
/account/-1
    */
    @Test
    public void save_wrongPathVar_400() throws Exception {
        String customerJson = "{\"firstName\": \"joe\",\"lastName\": \"doe\",\"address\":\"dublin\",\"phoneNumber\": \"012222222\",\"ssn\": \"AAA112233\"}";
        mockMvc.perform(put("/customer/1")
                .content(customerJson)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        String accountJson = "{\"accountType\": \"CHECKING\",\"amount\": \"100.22\",\"customer\": {\"id\": 1}}";
        MvcResult result = mockMvc.perform(put("/account/-1")
                .content(accountJson)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();
        assertTrue(result.getResolvedException() instanceof ConstraintViolationException);

        verify(mockRepository, times(0)).save(any(Account.class));
    }
}
