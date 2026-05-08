package ind.poc.demo.webclient.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseFreezeQuantity {
    private boolean isSuccess;
    private String errorMessage;
}
