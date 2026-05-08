package ind.poc.demo.service;

import ind.poc.demo.data.ResponseAction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
@Service
@RequiredArgsConstructor
public class IdempotenceService {

    private final RedisService redisService;
    private final PaymentsService paymentsService;

    public Object checkCompletePay(String paymentId){
        return redisService.get("complete:payment:" + paymentId);
    }

    public boolean lockCompletePay(String paymentId){
        return redisService.tryLock("lock:payment:" + paymentId, paymentId, 5000L);
    }

    public void releaseCompletePay(String paymentId){
        redisService.releaseLock("lock:payment:" + paymentId, paymentId);
    }
    public ResponseAction completePayment(String paymentId){
        ResponseAction responseAction;
        Object result = checkCompletePay(paymentId);
        if(result == null){
            final boolean lockIsGet = lockCompletePay(paymentId);
            if(lockIsGet) {
                try {
                    result = checkCompletePay(paymentId);
                    if(result == null) {
                        boolean isSuccess = paymentsService.completePayment(paymentId);
                        responseAction = ResponseAction.builder()
                                .isSuccess(isSuccess)
                                .build();
                    } else {
                        responseAction = (ResponseAction) result;
                    }
                } catch (Exception e) {
                    responseAction = ResponseAction.builder()
                            .isSuccess(false)
                            .errorMessage(e.getMessage())
                            .build();
                } finally {
                    releaseCompletePay(paymentId);
                }
            } else {
                responseAction = ResponseAction.builder()
                        .isSuccess(false)
                        .errorMessage("Duplicated request!!")
                        .build();
            }
        } else {
            responseAction = (ResponseAction) result;
        }
        return responseAction;
    }
}
