package ind.poc.demo.controller;

import ind.poc.demo.data.FlowPurchaseData;
import ind.poc.demo.data.RequestCreatePO;
import ind.poc.demo.data.ResponseCreatePO;
import ind.poc.demo.service.IdempotenceService;
import ind.poc.demo.service.KafkaProducerService;
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
    private final KafkaProducerService kafkaProducerService;

    @PostMapping("/new")
    public ResponseCreatePO onPurchaseOrder(@RequestBody RequestCreatePO requestCreatePO) {
        return idempotenceService.initOrder(requestCreatePO);
    }

    @PostMapping("/new-for-dlq")
    public ResponseCreatePO onPurchaseOrderDLQ(@RequestBody RequestCreatePO requestCreatePO) {
        final FlowPurchaseData build = FlowPurchaseData.builder()
                .orderId(requestCreatePO.getOrderId())
                .userId(requestCreatePO.getUserId())
                .partNo(requestCreatePO.getPartNo())
                .quantity(requestCreatePO.getQuantity())
                .build();
        kafkaProducerService.produceCreateNewOrder(build);
        return ResponseCreatePO.builder()
                .orderId(requestCreatePO.getOrderId())
                .build();
    }
}
