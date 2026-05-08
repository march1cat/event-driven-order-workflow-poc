package ind.poc.demo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class KafkaProducer {


    private final KafkaTemplate<String, Object> kafkaTemplate;
    @Value("${spring.kafka-topics.event-init-order-complete}")
    private String topicCompleteAddNewOrder;
    @Value("${spring.kafka-topics.event-complete-payment-done}")
    private String topicCloseOrder;

    @Value("${spring.kafka-topics.event-init-order-fail}")
    private String topicNewOrderFail;
    public void produceCompleteNewOrder(Object data){
        kafkaTemplate.send(topicCompleteAddNewOrder, data);
    }
    public void produceCloseOrder(Object data){
        kafkaTemplate.send(topicCloseOrder, data);
    }

    public void produceNewOrderFail(Object data){
        kafkaTemplate.send(topicNewOrderFail, data);
    }
}
