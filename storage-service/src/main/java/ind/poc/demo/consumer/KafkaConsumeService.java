package ind.poc.demo.consumer;

import ind.poc.demo.data.FailMessage;
import ind.poc.demo.data.FlowPurchaseData;
import ind.poc.demo.service.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class KafkaConsumeService {
    private final StorageService databaseAccessService;

    @KafkaListener(
            topics = "${spring.kafka-topics.event-complete-payment-new}",
            groupId = "${spring.consumer.kafka-group-id}"
    )
    public void consumeOrder(@Payload FlowPurchaseData data) {
        try {
            databaseAccessService.commitStorage(data.getOrderId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @KafkaListener(
            topics = "${spring.kafka-topics.event-init-order-payment-create-fail}",
            groupId = "${spring.consumer.kafka-group-id}"
    )
    public void consumeFailCreatePayment(@Payload FailMessage data) {
        try {
            databaseAccessService.rollbackFreeze(data.getOrderId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
