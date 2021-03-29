package bankapp.message;

import bankapp.model.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import java.util.Date;

@Component
@Profile("!test")
public class JmsConsumer implements MessageListener {

    Logger logger = LoggerFactory.getLogger(JmsConsumer.class);

    @Override
    @JmsListener(destination = "${active-mq.topic:message}")
    public void onMessage(Message message) {
        ObjectMessage objectMessage = (ObjectMessage) message;
        try {
            Customer employee = (Customer) objectMessage.getObject();
            logger.info(employee.toString() + " : " + new Date().toString());
        } catch (JMSException e) {
            logger.error(e.getMessage(), e);
        }
    }
}