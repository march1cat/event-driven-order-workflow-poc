package ind.poc.demo.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.concurrent.atomic.AtomicBoolean;

@Data
@Builder
@AllArgsConstructor
public class InitOrderFlowRecord {
    private String orderId;
    private String partNo;
    private String userId;
    private String paymentId;
    private int quantity;
    private AtomicBoolean isOrderCreated;
    private AtomicBoolean isPaymentCreated;
}
