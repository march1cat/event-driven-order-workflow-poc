package ind.poc.demo.webclient.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class RequestFreezeStorage {
    private String orderId;
    private String partNo;
    private int quantity;
}
