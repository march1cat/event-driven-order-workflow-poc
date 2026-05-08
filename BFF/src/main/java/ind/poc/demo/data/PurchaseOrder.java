package ind.poc.demo.data;

import lombok.Data;

@Data
public class PurchaseOrder {
    private String userId;
    private String partNo;
    private int quantity;
}
