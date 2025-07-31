package gift.repository.member;

import gift.domain.member.SocialMember;
import gift.entity.member.SocialMemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SocialMemberRepository extends JpaRepository<SocialMemberEntity, Long> {
    Optional<SocialMemberEntity> findByProviderIdAndProvider(Long providerId, SocialMember.Provider provider);
}