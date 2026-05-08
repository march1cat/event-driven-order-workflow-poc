package ind.poc.demo.data;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ProductItemLeftQuantity") // Replace with your actual table name if different
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductItemQuantity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "Id", length = 36, nullable = false)
    private String Id;

    @Column(name = "PartNo", nullable = false, length = 36, unique = true)
    private String partNo;

    @Column(name = "Quantity", nullable = false)
    private Integer quantity;

    @Column(name = "FreezeQuantity", nullable = false)
    private Integer freezeQuantity;
}
