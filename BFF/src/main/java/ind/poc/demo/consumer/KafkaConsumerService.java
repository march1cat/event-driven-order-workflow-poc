package ind.poc.demo.consumer;

import ind.poc.demo.data.*;
import ind.poc.demo.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Log4j2
public class KafkaConsumerService {

    private final ConcurrentHashMap<String, DeferredResult<ResponseCreatePO>> createPOResponsePool;
    private final ConcurrentHashMap<String, DeferredResult<ResponseCompletePayment>> paymentResponsePool;
    private final RedisService redisService;
    @KafkaListener(
            topics = "${spring.kafka-topics.event-init-order-complete}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consumeOrder(@Payload FlowPurchaseData data) {
        redisService.publish("init_order_complete", new RedisMessage(data));
    }

    @KafkaListener(
            topics = "${spring.kafka-topics.event-complete-payment-done}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consumeCompletePayment(@Payload FlowPurchaseData data) {
        final DeferredResult<ResponseCompletePayment> responseCreatePODeferredResult = paymentResponsePool.remove(data.getOrderId());
        if(responseCreatePODeferredResult != null){
            responseCreatePODeferredResult.setResult(ResponseCompletePayment.builder()
                    .isSuccess(true)
                    .orderId(data.getOrderId())
                    .paymentId(data.getPaymentId())
                    .build());
        } else {
            log.warn("Deffered Result can't be found!!, " + data.getOrderId());
        }
    }

    @KafkaListener(
            topics = "${spring.kafka-topics.event-init-order-payment-create-fail}",
            groupId = "${spring.consumer.kafka-group-id}"
    )
    public void consumeFailCreatePayment(@Payload FailMessage data) {
        redisService.publish("init_order_fail", new RedisMessage(data));
    }
}
