package ind.poc.demo.data;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "PurchaseOrders") // Replace with your actual table name if different
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseOrder {
    @Id
    @Column(name = "Id", length = 36, nullable = false)
    private String Id;

    @Column(name = "UserId", nullable = false, length = 36)
    private String userId;

    @Column(name = "PartNo", nullable = false, length = 36)
    private String partNo;

    @Column(name = "Quantity")
    private int quantity;

    @Column(name = "IsComplete")
    private int isComplete;

    @Column(name = "IsCanceled")
    private int isCanceled;
}
