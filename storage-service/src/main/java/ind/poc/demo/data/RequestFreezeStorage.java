package ind.poc.demo.data;

import lombok.Data;

@Data
public class RequestFreezeStorage {

    private String orderId;
    private String partNo;
    private int quantity;
}
