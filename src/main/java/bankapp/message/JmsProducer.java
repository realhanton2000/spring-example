package bankapp.message;

import bankapp.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
public class JmsProducer {

    @Autowired
    JmsTemplate jmsTemplate;

    @Value("${active-mq.topic:message}")
    private String topic;

    public void sendMessage(Customer message) {
        jmsTemplate.convertAndSend(topic, message);
    }
}
