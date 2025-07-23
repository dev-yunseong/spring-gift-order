package gift.service;

import gift.dto.MemberRequestDto;
import gift.domain.Member;
import gift.entity.MemberEntity;
import gift.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member getMember(long memberId) {
        MemberEntity memberEntity = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 Member 입니다."));

        return memberEntity.toDomain();
    }

    public Member creatMember(MemberRequestDto memberRequestDto) {

        if (memberRepository.findByEmail(memberRequestDto.email()).isPresent()){
            throw new IllegalArgumentException("email이 중복 됩니다.");
        }

        Member member = memberRequestDto.toDomain();
        MemberEntity memberEntity = new MemberEntity(member);
        MemberEntity savedMemberEntity = memberRepository.save(memberEntity);

        return savedMemberEntity.toDomain();
    }

    @Transactional(readOnly = true)
    public Member login(MemberRequestDto memberRequestDto) {
        MemberEntity memberEntity = memberRepository.findByEmail(memberRequestDto.email())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 멤버입니다."));

        Member member = memberEntity.toDomain();

        if (!member.validatePlainPassword(memberRequestDto.password())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return member;
    }
}
