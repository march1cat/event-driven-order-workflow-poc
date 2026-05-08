package ind.poc.demo.producer;

import ind.poc.demo.data.FlowPurchaseData;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, FlowPurchaseData> kafkaTemplate;
    @Value("${spring.kafka-topics.event-complete-payment-storage-commit}")
    private String topicCompleteWholeOrder;

    public void sendToCompleteWholeOrder(FlowPurchaseData data){
        kafkaTemplate.send(topicCompleteWholeOrder, data);
    }
}
