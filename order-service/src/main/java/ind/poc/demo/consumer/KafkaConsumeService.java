package ind.poc.demo.consumer;

import ind.poc.demo.data.FailMessage;
import ind.poc.demo.data.FlowPurchaseData;
import ind.poc.demo.service.KafkaProducerService;
import ind.poc.demo.service.PurchaseOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class KafkaConsumeService {

    private final PurchaseOrderService purchaseOrderService;

    @KafkaListener(
            topics = "${spring.kafka-topics.event-complete-payment-new}",
            groupId = "${spring.consumer.kafka-group-id}"
    )
    public void consumeCompletePayment(@Payload FlowPurchaseData data) {
        try {
            purchaseOrderService.completePurchaseOrder(data);
        } catch (Exception e) {
            //TODO
        }
    }

    @KafkaListener(
            topics = "${spring.kafka-topics.event-init-order-payment-create-fail}",
            groupId = "${spring.consumer.kafka-group-id}"
    )
    public void consumeFailCreatePayment(@Payload FailMessage data) {
        boolean isDone = purchaseOrderService.cancelOrder(data.getOrderId());
        if(!isDone){
            log.error("Cancel order fail!!");
        }
    }

}
