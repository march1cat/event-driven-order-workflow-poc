package ind.poc.demo.repository;

import ind.poc.demo.data.StorageOutHistory;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StorageOutHistoryRepository extends JpaRepository<StorageOutHistory, String> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "SELECT s FROM StorageOutHistory s WHERE s.orderId=:orderId and s.isComplete=0 and s.isCanceled=0")
    StorageOutHistory findbyOrder(@Param("orderId") String orderId);
}
