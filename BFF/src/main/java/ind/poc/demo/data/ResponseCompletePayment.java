package ind.poc.demo.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseCompletePayment {
    private boolean isSuccess;
    private String orderId;
    private String paymentId;
    private String errorMessage;
}
