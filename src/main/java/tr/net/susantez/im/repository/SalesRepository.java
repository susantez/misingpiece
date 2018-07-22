package tr.net.susantez.im.repository;

import tr.net.susantez.im.domain.Sales;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Sales entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SalesRepository extends JpaRepository<Sales, Long> {

    @Query(value = "select distinct sales from Sales sales left join fetch sales.events",
        countQuery = "select count(distinct sales) from Sales sales")
    Page<Sales> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct sales from Sales sales left join fetch sales.events")
    List<Sales> findAllWithEagerRelationships();

    @Query("select sales from Sales sales left join fetch sales.events where sales.id =:id")
    Optional<Sales> findOneWithEagerRelationships(@Param("id") Long id);

}
