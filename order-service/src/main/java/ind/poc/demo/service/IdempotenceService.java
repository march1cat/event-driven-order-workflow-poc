package ind.poc.demo.service;

import ind.poc.demo.data.RequestCreatePO;
import ind.poc.demo.data.ResponseCreatePO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IdempotenceService {

    private final RedisService redisService;
    private final PurchaseOrderService purchaseOrderService;

    public Object checkRequestOrder(String orderId){
        return redisService.get("init:order:" + orderId);
    }

    public boolean lockInitOrder(String orderId){
        return redisService.tryLock("lock:order:" + orderId, orderId, 5000L);
    }

    public void releaseInitOrder(String orderId){
        redisService.releaseLock("lock:order:" + orderId, orderId);
    }

    public ResponseCreatePO initOrder(RequestCreatePO requestCreatePO){
        ResponseCreatePO responseCreatePO;
        Object result = checkRequestOrder(requestCreatePO.getOrderId());
        if(result == null) {
            boolean isGettingLock = lockInitOrder(requestCreatePO.getOrderId());
            if(isGettingLock) {
                try {
                    result = checkRequestOrder(requestCreatePO.getOrderId());
                    if(result == null) {
                        final String oderId = purchaseOrderService.createPurchaseFlow(requestCreatePO.getOrderId(), requestCreatePO.getUserId(), requestCreatePO.getPartNo(), requestCreatePO.getQuantity());
                        responseCreatePO = ResponseCreatePO.builder()
                                .orderId(oderId)
                                .build();
                        redisService.set("init:order:" + requestCreatePO.getOrderId(),  responseCreatePO, 5000L);
                    } else {
                        responseCreatePO = (ResponseCreatePO) result;
                    }
                } catch (Exception e) {
                    responseCreatePO = ResponseCreatePO.builder()
                            .errorMessage(e.getMessage())
                            .build();
                } finally {
                    releaseInitOrder(requestCreatePO.getOrderId());
                }
            } else {
                responseCreatePO = ResponseCreatePO.builder()
                        .errorMessage("Duplicated request")
                        .build();
            }
        } else {
            responseCreatePO = (ResponseCreatePO) result;
        }
        return responseCreatePO;
    }
}
