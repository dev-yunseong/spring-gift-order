package gift.repository;

import gift.entity.OptionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptionRepository extends JpaRepository<OptionEntity, Long> {
    Page<OptionEntity> findAllByProductEntityId(long productId, Pageable pageable);
}
