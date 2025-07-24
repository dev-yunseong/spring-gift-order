package gift.repository.member;

import gift.entity.member.EmailMemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface EmailMemberRepository extends JpaRepository<EmailMemberEntity, Long> {
    Optional<EmailMemberEntity> findByEmail(String email);
}