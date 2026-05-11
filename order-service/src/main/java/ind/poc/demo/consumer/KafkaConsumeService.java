package ind.poc.demo.consumer;

import ind.poc.demo.data.FailMessage;
import ind.poc.demo.data.FlowPurchaseData;
import ind.poc.demo.error.OrderRecordNotFoundException;
import ind.poc.demo.service.KafkaProducerService;
import ind.poc.demo.service.PurchaseOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.kafka.retrytopic.TopicSuffixingStrategy;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.retry.annotation.Backoff;
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
    @RetryableTopic(
            attempts = "2",                                 // 總共嘗試 4 次 (1次原始 + 3次重試)
            backoff = @Backoff(delay = 2000, multiplier = 2.0), // 第一次等 2s，之後 4s, 8s (指數退避)
            autoCreateTopics = "true",                      // 自動建立重試與 DLQ topics
            topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE,
            dltStrategy = DltStrategy.FAIL_ON_ERROR,        // 失敗後進入 DLT
            include = {OrderRecordNotFoundException.class},             // 只有特定異常才重試
            retryTopicSuffix = "-cancelOrder-retry",
            dltTopicSuffix = "-cancelOrder-dlq"
    )
    @KafkaListener(
            topics = "${spring.kafka-topics.event-init-order-payment-create-fail}",
            groupId = "${spring.consumer.kafka-group-id}"
    )
    public void consumeFailCreatePayment(@Payload FailMessage data) {
        purchaseOrderService.cancelOrder(data.getOrderId());
    }

}
