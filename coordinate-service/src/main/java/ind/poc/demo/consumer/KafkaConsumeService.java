package ind.poc.demo.consumer;

import ind.poc.demo.data.FailMessage;
import ind.poc.demo.data.FlowPurchaseData;
import ind.poc.demo.service.KafkaProducer;
import ind.poc.demo.service.OrderOrdinateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
@Service
@RequiredArgsConstructor
@Log4j2
public class KafkaConsumeService {

    private final OrderOrdinateService orderOrdinateService;
    private final KafkaProducer kafkaProducer;
    @KafkaListener(
            topics = "${spring.kafka-topics.event-init-order-new}",
            groupId = "${spring.consumer.kafka-group-id}"
    )
    public void consumeCompleteNewOrder(@Payload FlowPurchaseData data) {
        orderOrdinateService.onEventCreateOrder(data.getOrderId());
    }

    @KafkaListener(
            topics = "${spring.kafka-topics.event-init-order-payment-create}",
            groupId = "${spring.consumer.kafka-group-id}"
    )
    public void consumeCompletePayment(@Payload FlowPurchaseData data) {
        log.info(data);
        orderOrdinateService.onEventCreatePayment(data.getOrderId(), data.getPaymentId());
    }

    @KafkaListener(
            topics = "${spring.kafka-topics.event-complete-payment-new}",
            groupId = "${spring.consumer.kafka-group-id}"
    )
    public void consumeClosePayment(@Payload FlowPurchaseData data) {
        orderOrdinateService.onEventInitClosePayment(data.getOrderId(), data.getPaymentId());
    }

    @KafkaListener(
            topics = "${spring.kafka-topics.event-complete-payment-order}",
            groupId = "${spring.consumer.kafka-group-id}"
    )
    public void consumeCloseOrder(@Payload FlowPurchaseData data) {
        orderOrdinateService.onEventCloseOrder(data.getOrderId());
    }

    @KafkaListener(
            topics = "${spring.kafka-topics.event-complete-payment-storage-commit}",
            groupId = "${spring.consumer.kafka-group-id}"
    )
    public void consumeStorageCommited(@Payload FlowPurchaseData data) {
        orderOrdinateService.onEventStorageCommited(data.getOrderId());
    }

    @KafkaListener(
            topics = "${spring.kafka-topics.event-init-order-payment-create-fail}",
            groupId = "${spring.consumer.kafka-group-id}"
    )
    public void consumeFailCreatePayment(@Payload FailMessage data) {
        kafkaProducer.produceNewOrderFail(data);
    }

}
