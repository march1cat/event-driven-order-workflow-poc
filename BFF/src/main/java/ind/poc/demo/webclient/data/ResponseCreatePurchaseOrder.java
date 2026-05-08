package ind.poc.demo.webclient.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseCreatePurchaseOrder {
    private String orderId;
    private String errorMessage;
}
