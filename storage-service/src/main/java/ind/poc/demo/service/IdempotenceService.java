package ind.poc.demo.service;

import ind.poc.demo.data.RequestFreezeStorage;
import ind.poc.demo.data.ResponseFreezeQuantity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IdempotenceService {

    private final RedisService redisService;
    private final StorageService databaseAccessService;
    public Object checkFreezeStorage(String orderId){
        return redisService.get("freeze:" + orderId);
    }

    public boolean lockFreezeStorage(String orderId){
        return redisService.tryLock("lock:storage:" + orderId, orderId, 5000L);
    }

    public void releaseInitOrder(String orderId){
        redisService.releaseLock("lock:storage:" + orderId, orderId);
    }

    public ResponseFreezeQuantity  freezeQuantity(RequestFreezeStorage requestFreezeStorage){
        ResponseFreezeQuantity resData;
        Object cacheResult = checkFreezeStorage(requestFreezeStorage.getOrderId());
        if(cacheResult == null) {
            boolean isGettingLock = lockFreezeStorage(requestFreezeStorage.getOrderId());
            if(isGettingLock){
                try {
                    boolean result = databaseAccessService.freezeQuantity(requestFreezeStorage.getOrderId(), requestFreezeStorage.getPartNo(), requestFreezeStorage.getQuantity());
                    resData = ResponseFreezeQuantity.builder().isSuccess(result).build();
                    redisService.set("freeze:storage:" + requestFreezeStorage.getOrderId(), "true", 5000L);
                } catch (Exception e) {
                    resData = ResponseFreezeQuantity.builder().isSuccess(false).errorMessage(e.getMessage()).build();
                } finally {
                    releaseInitOrder(requestFreezeStorage.getOrderId());
                }
            } else {
                resData = ResponseFreezeQuantity.builder().isSuccess(false).errorMessage("Duplicated request").build();
            }
        } else {
            resData = (ResponseFreezeQuantity) cacheResult;
        }
        return resData;
    }



}
