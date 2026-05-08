package ind.poc.demo.data;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Payments") // Replace with your actual table name if different
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "Id", length = 36, nullable = false)
    private String Id;

    @Column(name = "OderId", nullable = false, length = 36)
    private String orderId;

    @Column(name = "UserId", nullable = false, length = 36)
    private String userId;

    @Column(name = "IsPaid")
    private int isPaid;

    @Column(name = "IsCanceled")
    private int isCanceled;
}
