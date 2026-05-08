package ind.poc.demo.service;

import ind.poc.demo.data.RedisMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    public void publish(String channel, RedisMessage data) {
        // timeout 單位設為秒
        redisTemplate.convertAndSend(channel, data);
    }
}
