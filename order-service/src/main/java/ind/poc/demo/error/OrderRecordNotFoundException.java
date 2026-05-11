package ind.poc.demo.error;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class OrderRecordNotFoundException extends RuntimeException {
    private String orderId;
}
