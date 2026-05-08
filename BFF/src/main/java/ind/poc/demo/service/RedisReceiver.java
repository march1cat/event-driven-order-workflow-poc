package ind.poc.demo.service;

import ind.poc.demo.data.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
@Log4j2
public class RedisReceiver {

    private final ConcurrentHashMap<String, DeferredResult<ResponseCreatePO>> createPOResponsePool;
    private final ConcurrentHashMap<String, DeferredResult<ResponseCompletePayment>> paymentResponsePool;

    public void onCompleteNewOrder(RedisMessage<FlowPurchaseData> message) {
        log.info("on redis message: " + message);
        FlowPurchaseData data = message.getData();
        String key = data.getOrderId();

        DeferredResult<ResponseCreatePO> waiting = createPOResponsePool.get(key);
        if (waiting != null) {
            final ResponseCreatePO resData = ResponseCreatePO.builder()
                    .isSuccess(true)
                    .orderId(data.getOrderId())
                    .paymentId(data.getPaymentId()).build();
            waiting.setResult(resData);
            // 這裡不需要手動移除，因為 DeferredResult 的 onCompletion 會自己清理
        }
    }

    public void onFailNewOrder(RedisMessage<FailMessage> message) {
        log.info("on redis message: " + message);
        FailMessage data = message.getData();
        String key = data.getOrderId();

        DeferredResult<ResponseCreatePO> waiting = createPOResponsePool.get(key);
        if (waiting != null) {
            final ResponseCreatePO resData = ResponseCreatePO.builder()
                    .isSuccess(false)
                    .orderId(data.getOrderId())
                    .errorMessage(data.getMessage())
                    .build();
            waiting.setResult(resData);
            // 這裡不需要手動移除，因為 DeferredResult 的 onCompletion 會自己清理
        }
    }
}