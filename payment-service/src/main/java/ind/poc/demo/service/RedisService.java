package ind.poc.demo.service;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.concurrent.TimeUnit;
@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final DefaultRedisScript<Long> redisLockScript;
    private final DefaultRedisScript<Long> redisUnLockScript;

    public boolean tryLock(String lockKey,String requestId, long expireTime)  {
        Long result = redisTemplate.execute(redisLockScript, Collections.singletonList(lockKey), requestId, expireTime);
        return Long.valueOf(1L).equals(result);
    }

    public void releaseLock(String lockKey,String requestId)  {
        redisTemplate.execute(redisUnLockScript, Collections.singletonList(lockKey), requestId);
    }

    // 儲存一般鍵值對
    public void set(String key, Object value, long timeout) {
        // timeout 單位設為秒
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
    }

    // 獲取資料
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    // 刪除資料
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    // 檢查是否存在
    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
}
