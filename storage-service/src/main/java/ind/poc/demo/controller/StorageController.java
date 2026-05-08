package ind.poc.demo.controller;

import ind.poc.demo.data.RequestFreezeStorage;
import ind.poc.demo.data.ResponseFreezeQuantity;
import ind.poc.demo.service.IdempotenceService;
import ind.poc.demo.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/storage")
public class StorageController {

    private final IdempotenceService idempotenceService;

    @PostMapping("/freeze")
    public ResponseFreezeQuantity freezeStorage(@RequestBody RequestFreezeStorage requestFreezeStorage) {
        return idempotenceService.freezeQuantity(requestFreezeStorage);
    }
}
