package ind.poc.demo.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ResponseFreezeQuantity {
    private boolean isSuccess;
    private String errorMessage;
}
