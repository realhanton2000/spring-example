package bankapp;

import bankapp.model.Customer;
import bankapp.repository.CustomerRepository;
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
public class CustomerControllerMockRepoTest {

    private static final ObjectMapper om = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerRepository mockRepository;

    /*
{
    "lastName": "doe",
    "address": "dublin",
    "phoneNumber": "012222222",
    "ssn": "AAA112233"
}
     */
    @Test
    public void save_emptyFirstName_emptyLastName_400() throws Exception {
        String json = "{\"lastName\": \"doe\",\"address\":\"dublin\",\"phoneNumber\": \"012222222\",\"ssn\": \"AAA112233\"}";
        mockMvc.perform(put("/customer/1")
                .content(json)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp", is(notNullValue())))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.message", is("First name is required")));

        verify(mockRepository, times(0)).save(any(Customer.class));
    }

    /*
{
    "firstName": "joe",
    "lastName": "doe",
    "address": "dublin",
    "phoneNumber": "012222222",
    "ssn": "AAA112233"
}
 */
    @Test
    public void save_successful_200() throws Exception {
        String json = "{\"firstName\": \"joe\",\"lastName\": \"doe\",\"address\":\"dublin\",\"phoneNumber\": \"012222222\",\"ssn\": \"AAA112233\"}";
        mockMvc.perform(put("/customer/1")
                .content(json)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        verify(mockRepository, times(1)).save(any(Customer.class));
    }

    /*
{
    "firstName": "joe",
    "lastName": "doe",
    "address": "dublin",
    "phoneNumber": "012222222",
    "ssn": "AAA112233"
}
/customer/-1
 */
    @Test
    public void save_wrongPathVar_400() throws Exception {
        String json = "{\"firstName\": \"joe\",\"lastName\": \"doe\",\"address\":\"dublin\",\"phoneNumber\": \"012222222\",\"ssn\": \"AAA112233\"}";
        MvcResult result = mockMvc.perform(put("/customer/-1")
                .content(json)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();
        assertTrue(result.getResolvedException() instanceof ConstraintViolationException);

        verify(mockRepository, times(0)).save(any(Customer.class));
    }
}
