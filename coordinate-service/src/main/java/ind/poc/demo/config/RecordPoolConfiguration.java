package ind.poc.demo.config;

import ind.poc.demo.data.ClosePaymentFlowRecord;
import ind.poc.demo.data.InitOrderFlowRecord;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class RecordPoolConfiguration {
    @Bean
    public ConcurrentHashMap<String, InitOrderFlowRecord> initOrderFLow(){
        return new ConcurrentHashMap<String, InitOrderFlowRecord>();
    }
    @Bean
    public ConcurrentHashMap<String, ClosePaymentFlowRecord> initCloseOrderFLow(){
        return new ConcurrentHashMap<String, ClosePaymentFlowRecord>();
    }
}
