package ind.poc.demo.config;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import ind.poc.demo.service.RedisReceiver;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.concurrent.Executor;

@Configuration
@Log4j2
@RequiredArgsConstructor
public class RedisConfiguration {
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // 使用與 Listener 一致的 Jackson 序列化器
        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer();

        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(serializer);
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(serializer);

        return template;
    }
    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
                                            MessageListenerAdapter listenerCompleteNewOrder,
                                            MessageListenerAdapter listenerFailNewOrder) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        // 訂閱名為 "cache_refresh" 的頻道
        container.addMessageListener(listenerCompleteNewOrder, new PatternTopic("init_order_complete"));
        container.addMessageListener(listenerFailNewOrder, new PatternTopic("init_order_fail"));
        return container;
    }

    @Bean
    MessageListenerAdapter listenerCompleteNewOrder(RedisReceiver receiver) {
        // 指定由 RedisReceiver 的 "receiveMessage" 方法處理
        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer();
        MessageListenerAdapter adapter = new MessageListenerAdapter(receiver, "onCompleteNewOrder");
        // 關鍵：設定序列化器，否則它會用預設的 JDK 序列化去解析 JSON，導致解析失敗
        adapter.setSerializer(serializer);
        return adapter;
    }

    @Bean
    MessageListenerAdapter listenerFailNewOrder(RedisReceiver receiver) {
        // 指定由 RedisReceiver 的 "receiveMessage" 方法處理
        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer();
        MessageListenerAdapter adapter = new MessageListenerAdapter(receiver, "onFailNewOrder");
        // 關鍵：設定序列化器，否則它會用預設的 JDK 序列化去解析 JSON，導致解析失敗
        adapter.setSerializer(serializer);
        return adapter;
    }
}
