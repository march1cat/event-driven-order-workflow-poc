package ind.poc.demo.webclient;
import ind.poc.demo.webclient.data.RequestCreatePO;
import ind.poc.demo.webclient.data.ResponseCreatePurchaseOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "order-service")
public interface PurchaseOrderServiceClient {
    @PostMapping("/api/purchase/new")
    ResponseCreatePurchaseOrder onNewOrder(@RequestBody RequestCreatePO request);

    @PostMapping("/api/purchase/new-for-dlq")
    ResponseCreatePurchaseOrder onNewOrderDLQ(@RequestBody RequestCreatePO request);
}
