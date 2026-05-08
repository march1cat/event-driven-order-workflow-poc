package ind.poc.demo.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ResponseCreatePO {
    private boolean isSuccess;
    private String orderId;
    private String paymentId;
    private String errorMessage;
}
