package ind.poc.demo.data;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "StorageOutHistory") // Replace with your actual table name if different
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StorageOutHistory {

    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "Id", length = 36, nullable = false)
    private String Id;

    @Column(name = "PartNo", nullable = false, length = 36)
    private String partNo;

    @Column(name = "OutQuantity", nullable = false)
    private Integer outQuantity;

    @Column(name = "OrderId", nullable = false, length = 36, unique = true)
    private String orderId;

    @Column(name = "IsComplete")
    private int isComplete;

    @Column(name = "IsCanceled")
    private int isCanceled;
}
