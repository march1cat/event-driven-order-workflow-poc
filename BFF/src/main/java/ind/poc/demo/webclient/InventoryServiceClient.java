package ind.poc.demo.webclient;

import ind.poc.demo.webclient.data.RequestFreezeStorage;
import ind.poc.demo.webclient.data.ResponseFreezeQuantity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "inventory-service")
public interface InventoryServiceClient {
    @PostMapping("/api/storage/freeze")
    ResponseFreezeQuantity freezeStorage(@RequestBody RequestFreezeStorage request);
}
