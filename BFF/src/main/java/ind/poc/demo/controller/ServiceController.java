package ind.poc.demo.controller;

import ind.poc.demo.data.*;
import ind.poc.demo.service.ComposeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


@RestController
@RequestMapping("/api/purchase")
@RequiredArgsConstructor
@Log4j2
public class ServiceController {

    private final ComposeService composeService;
    private final ConcurrentHashMap<String, DeferredResult<ResponseCreatePO>> createPOResponsePool;
    private final ConcurrentHashMap<String, DeferredResult<ResponseCompletePayment>> paymentResponsePool;

    @PostMapping("/create")
    public DeferredResult<ResponseCreatePO> onNewOrder(@RequestBody PurchaseOrder purchaseOrder){
        String tmpOrderId = UUID.randomUUID().toString();
        DeferredResult<ResponseCreatePO> result = new DeferredResult<>(10000L);
        result.onTimeout(() -> {
            result.setErrorResult(NewOrderTimeoutResponse.builder()
                    .requstId(tmpOrderId)
                    .requestPO(purchaseOrder)
                    .message("New order timeout")
                    .build());
        });
        result.onCompletion(() -> {
            createPOResponsePool.remove(tmpOrderId);
        });
        final ResponseCreatePO responseCreatePO = composeService.onNewPurchaseOrder(tmpOrderId, purchaseOrder.getUserId(), purchaseOrder.getPartNo(), purchaseOrder.getQuantity());
        if(!responseCreatePO.isSuccess()){
            result.setErrorResult(responseCreatePO.getErrorMessage());
        } else {
            createPOResponsePool.put(responseCreatePO.getOrderId(), result);
        }
        return result;
    }
    @PostMapping("/complete")
    public DeferredResult<ResponseCompletePayment>  onCompleteOrder(@RequestBody RequestDonePayment request){
        DeferredResult<ResponseCompletePayment> result = new DeferredResult<>();
        final ResponseAction responseAction = composeService.onCompletePayment(request.getPaymentId());
        if(!responseAction.isSuccess()) {
            result.setErrorResult(responseAction.getErrorMessage());
        } else {
            paymentResponsePool.put(request.getOrderId(), result);
        }
        return result;
    }

}
