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

    @PostMapping("/fake-freeze")
    public ResponseFreezeQuantity flowSimulateRollbackFail(@RequestBody RequestFreezeStorage requestFreezeStorage) {
        //Fake freeze storage, while the failing rollback is triggered, the storage unfreezed will be failed.
        return ResponseFreezeQuantity.builder()
                .isSuccess(true)
                .build();
    }
}
