package ind.poc.demo.repository;

import ind.poc.demo.data.ProductItemQuantity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductItemQuantityRepository extends JpaRepository< ProductItemQuantity, String> {
    @Modifying
    @Query("Update ProductItemQuantity p set p.freezeQuantity=p.freezeQuantity+:quantity where p.partNo=:partNo and p.quantity >= p.freezeQuantity+:quantity")
    int freezeQuantity(String partNo, int quantity);
    @Modifying
    @Query("Update ProductItemQuantity p set p.quantity=p.quantity-:quantity, p.freezeQuantity=p.freezeQuantity - :quantity where p.partNo=:partNo and p.quantity-:quantity >= 0 and p.freezeQuantity - :quantity >= 0")
    int commitQuantity(String partNo, int quantity);


    @Modifying
    @Query("Update ProductItemQuantity p set p.freezeQuantity=p.freezeQuantity-:quantity where p.partNo=:partNo")
    int rollbackQuantity(String partNo, int quantity);

}
