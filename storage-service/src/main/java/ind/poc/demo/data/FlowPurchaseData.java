package ind.poc.demo.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FlowPurchaseData{
    private String orderId;
    private String partNo;
    private String userId;
    private String paymentId;
    private int quantity;
}
