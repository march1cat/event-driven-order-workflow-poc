package ind.poc.demo.service;

import ind.poc.demo.data.FlowPurchaseData;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class KafkaProducerService {

    private final KafkaTemplate<String, FlowPurchaseData> kafkaTemplate;
    @Value("${spring.kafka-topics.event-init-order-new}")
    private String topicCompleteAddNewOrder;
    @Value("${spring.kafka-topics.event-complete-payment-order}")
    private String topicCloseOrder;
    public void produceCreateNewOrder(FlowPurchaseData data){
        kafkaTemplate.send(topicCompleteAddNewOrder, data);
    }
    public void produceCloseOrder(FlowPurchaseData data){
        kafkaTemplate.send(topicCloseOrder, data);
    }


}
