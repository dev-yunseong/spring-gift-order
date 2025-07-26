package gift.repository.member;

import gift.entity.member.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommonMemberRepository extends JpaRepository<MemberEntity, Long> {
}
