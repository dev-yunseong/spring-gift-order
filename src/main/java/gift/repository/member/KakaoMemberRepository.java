package gift.repository.member;

import gift.entity.member.KakaoMemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KakaoMemberRepository extends JpaRepository<KakaoMemberEntity, Long> {
    Optional<KakaoMemberEntity> findByKakaoId(Long kakaoId);
}