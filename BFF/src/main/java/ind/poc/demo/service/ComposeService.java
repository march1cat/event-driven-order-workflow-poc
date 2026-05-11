package ind.poc.demo.service;

import ind.poc.demo.data.ResponseAction;
import ind.poc.demo.data.ResponseCreatePO;
import ind.poc.demo.webclient.InventoryServiceClient;
import ind.poc.demo.webclient.PaymentServiceClient;
import ind.poc.demo.webclient.PurchaseOrderServiceClient;
import ind.poc.demo.webclient.data.RequestCreatePO;
import ind.poc.demo.webclient.data.RequestFreezeStorage;
import ind.poc.demo.webclient.data.ResponseCreatePurchaseOrder;
import ind.poc.demo.webclient.data.ResponseFreezeQuantity;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Log4j2
public class ComposeService {

    private final InventoryServiceClient inventoryServiceClient;
    private final PurchaseOrderServiceClient purchaseOrderServiceClient;
    private final PaymentServiceClient paymentServiceClient;
    public ResponseCreatePO onNewPurchaseOrder(String orderId, String userId, String partNo, int quantity){
        final RequestFreezeStorage freezeStorageData = RequestFreezeStorage.builder()
                .orderId(orderId)
                .partNo(partNo)
                .quantity(quantity)
                .build();

        final ResponseFreezeQuantity responseFreezeQuantity = inventoryServiceClient.freezeStorage(freezeStorageData);
        log.info(responseFreezeQuantity);
        if(responseFreezeQuantity.isSuccess()) {
            final RequestCreatePO request = RequestCreatePO.builder()
                    .orderId(orderId)
                    .partNo(partNo)
                    .userId(userId)
                    .quantity(quantity)
                    .build();
            final ResponseCreatePurchaseOrder responseCreatePO = purchaseOrderServiceClient.onNewOrder(request);
            if(responseCreatePO.getErrorMessage() != null) {
                return ResponseCreatePO.builder()
                        .isSuccess(false)
                        .errorMessage(responseCreatePO.getErrorMessage()).build();
            }
            return ResponseCreatePO.builder()
                    .isSuccess(true)
                    .orderId(responseCreatePO.getOrderId())
                    .build();
        } else {
            return ResponseCreatePO.builder()
                    .isSuccess(false)
                    .errorMessage(responseFreezeQuantity.getErrorMessage()).build();
        }
    }

    public ResponseCreatePO onNewPurchaseOrderForDLQ(String orderId, String userId, String partNo, int quantity){
        final RequestFreezeStorage freezeStorageData = RequestFreezeStorage.builder()
                .orderId(orderId)
                .partNo(partNo)
                .quantity(quantity)
                .build();
        final ResponseFreezeQuantity responseFreezeQuantity = inventoryServiceClient.fakefreezeStorage(freezeStorageData);
        if(responseFreezeQuantity.isSuccess()) {
            final RequestCreatePO request = RequestCreatePO.builder()
                    .orderId(orderId)
                    .partNo(partNo)
                    .userId(userId)
                    .quantity(quantity)
                    .build();
            final ResponseCreatePurchaseOrder responseCreatePO = purchaseOrderServiceClient.onNewOrderDLQ(request);
            if(responseCreatePO.getErrorMessage() != null) {
                return ResponseCreatePO.builder()
                        .isSuccess(false)
                        .errorMessage(responseCreatePO.getErrorMessage()).build();
            }
            return ResponseCreatePO.builder()
                    .isSuccess(true)
                    .orderId(responseCreatePO.getOrderId())
                    .build();
        } else {
            return ResponseCreatePO.builder()
                    .isSuccess(false)
                    .errorMessage(responseFreezeQuantity.getErrorMessage()).build();
        }
    }



    public ResponseAction onCompletePayment(String paymentId){
        final ResponseAction responseAction = paymentServiceClient.onCompletePayment(paymentId);
        return responseAction;
    }






}
