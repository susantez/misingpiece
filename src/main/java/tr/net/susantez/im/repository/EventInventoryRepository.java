package tr.net.susantez.im.repository;

import tr.net.susantez.im.domain.EventInventory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the EventInventory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EventInventoryRepository extends JpaRepository<EventInventory, Long> {

}
