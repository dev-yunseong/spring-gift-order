package gift.service;

import gift.domain.member.EmailMember;
import gift.dto.MemberRequestDto;
import gift.domain.member.Member;
import gift.entity.member.EmailMemberEntity;
import gift.entity.member.MemberEntity;
import gift.repository.EmailMemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MemberService {

    private final EmailMemberRepository emailMemberRepository;

    public MemberService(EmailMemberRepository emailMemberRepository) {
        this.emailMemberRepository = emailMemberRepository;
    }

    public Member getMember(long memberId) {
        MemberEntity memberEntity = emailMemberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 Member 입니다."));

        return memberEntity.toDomain();
    }

    public Member creatMember(MemberRequestDto memberRequestDto) {

        if (emailMemberRepository.findByEmail(memberRequestDto.email()).isPresent()){
            throw new IllegalArgumentException("email이 중복 됩니다.");
        }

        EmailMember member = memberRequestDto.toDomain();
        EmailMemberEntity memberEntity = new EmailMemberEntity(member);
        EmailMemberEntity savedMemberEntity = emailMemberRepository.save(memberEntity);

        return savedMemberEntity.toDomain();
    }

    @Transactional(readOnly = true)
    public Member login(MemberRequestDto memberRequestDto) {
        EmailMemberEntity memberEntity = emailMemberRepository.findByEmail(memberRequestDto.email())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 멤버입니다."));

        EmailMember member = memberEntity.toDomain();

        if (!member.validatePlainPassword(memberRequestDto.password())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return member;
    }
}
