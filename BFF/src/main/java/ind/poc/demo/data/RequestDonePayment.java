package ind.poc.demo.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestDonePayment {
    private String orderId;
    private String userId;
    private String paymentId;
}
