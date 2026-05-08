package ind.poc.demo.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.concurrent.atomic.AtomicBoolean;

@Data
@Builder
@AllArgsConstructor
public class ClosePaymentFlowRecord {
    private String orderId;
    private String partNo;
    private String userId;
    private String paymentId;
    private int quantity;
    private AtomicBoolean isOrderClosed;
    private AtomicBoolean isPaymentClosed;
    private AtomicBoolean isStorageCommited;
}
