package ind.poc.demo.service;

import ind.poc.demo.data.ClosePaymentFlowRecord;
import ind.poc.demo.data.FlowPurchaseData;
import ind.poc.demo.data.InitOrderFlowRecord;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@AllArgsConstructor
public class OrderOrdinateService {

    private final ConcurrentHashMap<String, InitOrderFlowRecord> recordNewOrderPool;
    private final ConcurrentHashMap<String, ClosePaymentFlowRecord> recordClosePaymentPool;
    private final KafkaProducer kafkaProducer;
    public void onEventCreateOrder(String orderId){
        InitOrderFlowRecord record = getNewOrderRecord(orderId);
        record.getIsOrderCreated().set(true);
        onEventCompleteCheck(record);
    }

    public void onEventCreatePayment(String orderId, String paymentId){
        InitOrderFlowRecord record = getNewOrderRecord(orderId);
        record.setPaymentId(paymentId);
        record.getIsPaymentCreated().set(true);
        onEventCompleteCheck(record);
    }
    private InitOrderFlowRecord getNewOrderRecord(String orderId){
        return recordNewOrderPool.computeIfAbsent(orderId, o -> InitOrderFlowRecord.builder()
                .isOrderCreated(new AtomicBoolean(false))
                .isPaymentCreated(new AtomicBoolean(false))
                .orderId(orderId)
                .build() );
    }

    private void onEventCompleteCheck(InitOrderFlowRecord record){
        if (record.getIsOrderCreated().get() && record.getIsPaymentCreated().get()) {
            kafkaProducer.produceCompleteNewOrder(FlowPurchaseData.builder()
                            .orderId(record.getOrderId())
                            .partNo(record.getPartNo())
                            .quantity(record.getQuantity())
                            .userId(record.getUserId())
                            .paymentId(record.getPaymentId())
                    .build());
            recordNewOrderPool.remove(record.getOrderId());
        }
    }

    public void onEventInitClosePayment(String orderId, String paymentId){
        ClosePaymentFlowRecord record = getRecordClosePaymentRecord(orderId);
        record.getIsPaymentClosed().set(true);
        record.setPaymentId(paymentId);
        onEventCompleteCheckClosePayment(record);
    }

    public void onEventCloseOrder(String orderId){
        ClosePaymentFlowRecord record = getRecordClosePaymentRecord(orderId);
        record.getIsOrderClosed().set(true);
        onEventCompleteCheckClosePayment(record);
    }

    public void onEventStorageCommited(String orderId){
        ClosePaymentFlowRecord record = getRecordClosePaymentRecord(orderId);
        record.getIsStorageCommited().set(true);
        onEventCompleteCheckClosePayment(record);
    }


    private ClosePaymentFlowRecord getRecordClosePaymentRecord(String orderId){
        return recordClosePaymentPool.computeIfAbsent(orderId, o -> ClosePaymentFlowRecord.builder()
                .orderId(orderId)
                .isPaymentClosed(new AtomicBoolean(false))
                .isOrderClosed(new AtomicBoolean(false))
                .isStorageCommited(new AtomicBoolean(false))
                .build());
    }

    private void onEventCompleteCheckClosePayment(ClosePaymentFlowRecord record){
        if (record.getIsPaymentClosed().get() && record.getIsOrderClosed().get() && record.getIsStorageCommited().get()) {
            kafkaProducer.produceCloseOrder(FlowPurchaseData.builder()
                    .orderId(record.getOrderId())
                    .partNo(record.getPartNo())
                    .quantity(record.getQuantity())
                    .userId(record.getUserId())
                    .build());
            recordNewOrderPool.remove(record.getOrderId());
        }
    }

}
