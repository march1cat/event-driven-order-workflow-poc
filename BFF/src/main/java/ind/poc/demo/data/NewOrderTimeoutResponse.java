package ind.poc.demo.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class NewOrderTimeoutResponse {
    private String requstId;
    private String message;
    private PurchaseOrder requestPO;
}
