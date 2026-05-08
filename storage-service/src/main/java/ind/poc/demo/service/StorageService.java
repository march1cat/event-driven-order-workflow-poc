package ind.poc.demo.service;

import ind.poc.demo.data.FlowPurchaseData;
import ind.poc.demo.data.StorageOutHistory;
import ind.poc.demo.producer.KafkaProducerService;
import ind.poc.demo.repository.ProductItemQuantityRepository;
import ind.poc.demo.repository.StorageOutHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
@RequiredArgsConstructor
public class StorageService {

    private final ProductItemQuantityRepository productItemQuantityRepository;
    private final StorageOutHistoryRepository storageOutHistoryRepository;
    private final KafkaProducerService kafkaProducerService;

    @Transactional
    public boolean freezeQuantity(String orderId, String partNo, int quantity){
        int affectedRownums = productItemQuantityRepository.freezeQuantity(partNo, quantity);
        if(affectedRownums == 0) {
            throw new RuntimeException("Storage not enough!!");
        }
        final StorageOutHistory storageOut = StorageOutHistory.builder()
                .outQuantity(quantity)
                .orderId(orderId)
                .partNo(partNo)
                .build();
        storageOutHistoryRepository.save(storageOut);
        return true;
    }
    @Transactional
    public void commitStorage(String orderId){
        final StorageOutHistory storageOutHistory = storageOutHistoryRepository.findbyOrder(orderId);
        if(storageOutHistory == null){
            throw new RuntimeException("Duplicated request");
        }

        int affectRows = productItemQuantityRepository.commitQuantity(storageOutHistory.getPartNo(), storageOutHistory.getOutQuantity());
        if(affectRows <= 0){
            throw new RuntimeException("Freezed quantity not found!!");
        }
        storageOutHistory.setIsComplete(1);
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                kafkaProducerService.sendToCompleteWholeOrder(FlowPurchaseData.builder()
                        .orderId(orderId)
                        .partNo(storageOutHistory.getPartNo())
                        .quantity(storageOutHistory.getOutQuantity())
                        .build());

            }
        });
    }

    @Transactional
    public void rollbackFreeze(String orderId){
        final StorageOutHistory storageOutHistory = storageOutHistoryRepository.findbyOrder(orderId);
        if(storageOutHistory == null){
            throw new RuntimeException("Duplicated request");
        }
        int affectRows = productItemQuantityRepository.rollbackQuantity(storageOutHistory.getPartNo(), storageOutHistory.getOutQuantity());
        if(affectRows <= 0){
            throw new RuntimeException("Freezed quantity not found!!");
        }
        storageOutHistory.setIsCanceled(1);
    }
}
