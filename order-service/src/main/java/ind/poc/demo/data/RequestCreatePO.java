package ind.poc.demo.data;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class RequestCreatePO {
    private String orderId;
    private String userId;
    private String partNo;
    private int quantity;
}
