package ind.poc.demo.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@AllArgsConstructor
@NoArgsConstructor
public class RedisMessage<T> implements Serializable {
    T data;
    public T getData() {
        return data;
    }
}
