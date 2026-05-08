package ind.poc.demo.controller;

import ind.poc.demo.data.ResponseAction;
import ind.poc.demo.service.IdempotenceService;
import ind.poc.demo.service.PaymentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class PaymentController {

    private final IdempotenceService idempotenceService;
    @PostMapping("/{paymentId}/complete")
    public ResponseAction pay(@PathVariable String paymentId) {
        return idempotenceService.completePayment(paymentId);
    }
}
