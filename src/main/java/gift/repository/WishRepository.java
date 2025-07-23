package gift.repository;

import gift.entity.WishEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WishRepository extends JpaRepository<WishEntity, Long> {
    Optional<WishEntity> findWishesByMemberEntityIdAndProductEntityId(long memberId, long productId);

    @EntityGraph(attributePaths = "productEntity")
    Page<WishEntity> findByMemberEntityId(long memberId, Pageable pageable);
}