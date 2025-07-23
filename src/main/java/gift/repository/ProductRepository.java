package gift.repository;

import gift.domain.Product;
import gift.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    @EntityGraph(attributePaths = "optionEntities")
    Optional<ProductEntity> findWithOptionsById(long id);

    @Query("""
        SELECT DISTINCT p
        FROM ProductEntity p
        JOIN p.optionEntities o
        WHERE p.status = :status
    """)
    Page<ProductEntity> findAllByStatusHavingOptions(@Param("status") Product.Status status, Pageable pageable);

}