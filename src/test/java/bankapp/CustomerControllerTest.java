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
public class CustomerControllerTest {

    private static final ObjectMapper om = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

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
    public void save_read_200() throws Exception {
        String json = "{\"firstName\": \"joe\",\"lastName\": \"doe\",\"address\":\"dublin\",\"phoneNumber\": \"012222222\",\"ssn\": \"AAA112233\"}";
        mockMvc.perform(put("/customer/1")
                .content(json)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(get("/customer/1")
                .content(json)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("joe")))
                .andExpect(jsonPath("$.lastName", is("doe")))
                .andExpect(jsonPath("$.address", is("dublin")))
                .andExpect(jsonPath("$.phoneNumber", is("012222222")));
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
    public void save_delete_200() throws Exception {
        String json = "{\"firstName\": \"joe\",\"lastName\": \"doe\",\"address\":\"dublin\",\"phoneNumber\": \"012222222\",\"ssn\": \"AAA112233\"}";
        mockMvc.perform(put("/customer/1")
                .content(json)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(delete("/customer/1")
                .content(json)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
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
    public void save_delete_read_404() throws Exception {
        String json = "{\"firstName\": \"joe\",\"lastName\": \"doe\",\"address\":\"dublin\",\"phoneNumber\": \"012222222\",\"ssn\": \"AAA112233\"}";
        mockMvc.perform(put("/customer/1")
                .content(json)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(delete("/customer/1")
                .content(json)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(get("/customer/1")
                .content(json)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(status().reason("Account doesn't exist."));
    }
}
