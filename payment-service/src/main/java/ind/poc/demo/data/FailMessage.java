package ind.poc.demo.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class FailMessage implements FlowData{
    private String orderId;
    private String message;
}
