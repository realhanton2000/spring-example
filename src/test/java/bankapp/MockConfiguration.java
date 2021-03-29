package bankapp;

import bankapp.message.JmsProducer;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
public class MockConfiguration {

    @Bean
    public JmsProducer jmsProducer() {
        return Mockito.mock(JmsProducer.class);
    }
}
