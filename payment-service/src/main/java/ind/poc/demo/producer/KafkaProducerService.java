package ind.poc.demo.producer;

import ind.poc.demo.data.FailMessage;
import ind.poc.demo.data.FlowData;
import ind.poc.demo.data.FlowPurchaseData;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, FlowData> kafkaTemplate;
    @Value("${spring.kafka-topics.event-init-order-payment-create}")
    private String topicAddPayment;

    @Value("${spring.kafka-topics.event-complete-payment-new}")
    private String topicCompletePaid;

    @Value("${spring.kafka-topics.event-init-order-payment-create-fail}")
    private String topicAddPaymentFail;

    public void sendToCompleteAddPayment(FlowPurchaseData data){
        kafkaTemplate.send(topicAddPayment, data);
    }

    public void sendToAddPaymentFail(FailMessage message){
        kafkaTemplate.send(topicAddPaymentFail, message);
    }
    public void sendToCompletePaid(FlowPurchaseData data){
        kafkaTemplate.send(topicCompletePaid, data);
    }
}
