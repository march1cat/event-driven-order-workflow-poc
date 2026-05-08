package ind.poc.demo.controller;

import ind.poc.demo.data.RequestCreatePO;
import ind.poc.demo.data.ResponseCreatePO;
import ind.poc.demo.service.IdempotenceService;
import ind.poc.demo.service.PurchaseOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping ("/api/purchase")
public class PurchaseOrderController {

    private final IdempotenceService idempotenceService;
    @PostMapping("/new")
    public ResponseCreatePO onPurchaseOrder(@RequestBody RequestCreatePO requestCreatePO) {
        return idempotenceService.initOrder(requestCreatePO);
    }
}
