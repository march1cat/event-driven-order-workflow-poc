package ind.poc.demo.consumer;

import ind.poc.demo.data.FailMessage;
import ind.poc.demo.data.FlowPurchaseData;
import ind.poc.demo.producer.KafkaProducerService;
import ind.poc.demo.service.PaymentsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class KafkaConsumeService {

    private final PaymentsService paymentsService;
    private final KafkaProducerService kafkaProducerService;

    @KafkaListener(
            topics = "${spring.kafka-topics.event-init-order-new}",
            groupId = "${spring.consumer.kafka-group-id}"
    )
    public void consumeOrder(@Payload FlowPurchaseData data) {
        try {
            if(data.getUserId().equals("Fail")) {
                throw new Exception("Simulate Error Happening!!");
            }
            String paymentId = paymentsService.createNewPayment(data.getOrderId(), data.getUserId());
            data.setPaymentId(paymentId);
            kafkaProducerService.sendToCompleteAddPayment(data);
        } catch (Exception e) {
            kafkaProducerService.sendToAddPaymentFail(FailMessage.builder()
                            .orderId(data.getOrderId())
                            .message(e.getMessage())
                    .build());
        }
    }

}
