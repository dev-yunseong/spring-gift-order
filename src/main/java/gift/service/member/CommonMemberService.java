package gift.service.member;

import gift.domain.member.Member;
import gift.entity.member.MemberEntity;
import gift.repository.member.CommonMemberRepository;
import org.springframework.stereotype.Service;

@Service
public class CommonMemberService {

    private final CommonMemberRepository memberRepository;

    public CommonMemberService(CommonMemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member getMember(long memberId) {
        MemberEntity memberEntity = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 Member 입니다."));

        return memberEntity.toDomain();
    }
}
