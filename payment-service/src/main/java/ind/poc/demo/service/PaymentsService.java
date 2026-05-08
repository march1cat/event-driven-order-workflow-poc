package ind.poc.demo.service;

import ind.poc.demo.data.FlowPurchaseData;
import ind.poc.demo.data.Payment;
import ind.poc.demo.producer.KafkaProducerService;
import ind.poc.demo.repository.PaymentsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentsService {

    private final PaymentsRepository paymentsRepository;
    private final KafkaProducerService kafkaProducerService;
    @Transactional
    public String createNewPayment(String orderId, String userId){
        final Payment payment = Payment.builder()
                .userId(userId)
                .orderId(orderId)
                .build();
        paymentsRepository.save(payment);
        return payment.getId();
    }
    @Transactional
    public boolean completePayment(String paymentId){
        final Optional<Payment> optionalPayment = paymentsRepository.findById(paymentId);
        if(optionalPayment.isPresent()) {
            Payment payment = optionalPayment.get();
            optionalPayment.get().setIsPaid(1);
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    final FlowPurchaseData flowData = FlowPurchaseData.builder()
                            .paymentId(paymentId)
                            .orderId(payment.getOrderId())
                            .userId(payment.getUserId())
                            .build();
                    kafkaProducerService.sendToCompletePaid(flowData);
                }
            });
            return true;
        } else {
            throw new RuntimeException("Payment not exist!!");
        }
    }

}
