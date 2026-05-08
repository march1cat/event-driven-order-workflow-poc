package ind.poc.demo.config;

import ind.poc.demo.data.ResponseCompletePayment;
import ind.poc.demo.data.ResponseCreatePO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class DeferredResultConfig {

    @Bean
    public ConcurrentHashMap<String, DeferredResult<ResponseCreatePO>> initCreatePRDefferedResult(){
        return new ConcurrentHashMap<String, DeferredResult<ResponseCreatePO>>();
    }

    @Bean
    public ConcurrentHashMap<String, DeferredResult<ResponseCompletePayment>> initCompletePaymentDefferedResult(){
        return new ConcurrentHashMap<String, DeferredResult<ResponseCompletePayment>>();
    }
}
