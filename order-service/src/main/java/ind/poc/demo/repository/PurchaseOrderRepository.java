package ind.poc.demo.repository;

import ind.poc.demo.data.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, String> {
    @Modifying
    @Query("Update PurchaseOrder p set p.isCanceled=1 where Id=:orderId and p.isComplete=0 and p.isCanceled=0")
    int cancelOrder(String orderId);
}
