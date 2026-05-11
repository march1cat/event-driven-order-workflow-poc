package ind.poc.demo.service;

import ind.poc.demo.data.FlowPurchaseData;
import ind.poc.demo.data.PurchaseOrder;
import ind.poc.demo.error.OrderRecordNotFoundException;
import ind.poc.demo.repository.PurchaseOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class PurchaseOrderService {
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final KafkaProducerService kafkaProducerService;
    @Transactional
    public String createPurchaseFlow(String orderId, String userId, String partNo, int quantity){
        final PurchaseOrder po = PurchaseOrder.builder()
                .Id(orderId)
                .userId(userId)
                .partNo(partNo)
                .quantity(quantity)
                .build();
        purchaseOrderRepository.save(po);
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                final FlowPurchaseData build = FlowPurchaseData.builder()
                        .orderId(po.getId())
                        .userId(po.getUserId())
                        .partNo(po.getPartNo())
                        .quantity(po.getQuantity())
                        .build();
                kafkaProducerService.produceCreateNewOrder(build);
            }
        });

        return po.getId();
    }
    @Transactional
    public boolean completePurchaseOrder(FlowPurchaseData flowPurchaseData){
        final Optional<PurchaseOrder> optionalPurchaseOrder = purchaseOrderRepository.findById(flowPurchaseData.getOrderId());
        if(optionalPurchaseOrder.isPresent()){
            PurchaseOrder order = optionalPurchaseOrder.get();
            order.setIsComplete(1);
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    kafkaProducerService.produceCloseOrder(FlowPurchaseData.builder()
                            .orderId(flowPurchaseData.getOrderId())
                            .partNo(order.getPartNo())
                            .quantity(order.getQuantity())
                            .userId(flowPurchaseData.getUserId())
                            .build());
                }
            });
            return true;
        } else {
            throw new RuntimeException("Order not found!!");
        }
    }

    @Transactional
    public void cancelOrder(String orderId){
        int affectedCount = purchaseOrderRepository.cancelOrder(orderId);
        if(affectedCount <= 0) {
            throw new OrderRecordNotFoundException(orderId);
        }
    }

}
