package ind.poc.demo.webclient;
import ind.poc.demo.data.ResponseAction;
import ind.poc.demo.webclient.data.RequestCreatePO;
import ind.poc.demo.webclient.data.ResponseCreatePurchaseOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "payment-service")
public interface PaymentServiceClient {
    @PostMapping("/api/payments/{paymentId}/complete")
    ResponseAction onCompletePayment(@PathVariable("paymentId") String paymentId);
}
