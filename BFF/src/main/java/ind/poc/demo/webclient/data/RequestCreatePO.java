package ind.poc.demo.webclient.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.checkerframework.checker.units.qual.A;

@Data
@Builder
@AllArgsConstructor
public class RequestCreatePO {
    private String orderId;
    private String userId;
    private String partNo;
    private int quantity;
}
