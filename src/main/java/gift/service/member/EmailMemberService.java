package gift.service.member;

import gift.domain.member.EmailMember;
import gift.dto.MemberRequestDto;
import gift.entity.member.EmailMemberEntity;
import gift.repository.member.EmailMemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EmailMemberService {

    private final EmailMemberRepository emailMemberRepository;

    public EmailMemberService(EmailMemberRepository emailMemberRepository) {
        this.emailMemberRepository = emailMemberRepository;
    }

    public EmailMember creatMember(MemberRequestDto memberRequestDto) {

        if (emailMemberRepository.findByEmail(memberRequestDto.email()).isPresent()){
            throw new IllegalArgumentException("email이 중복 됩니다.");
        }

        EmailMember member = memberRequestDto.toDomain();
        EmailMemberEntity memberEntity = new EmailMemberEntity(member);
        EmailMemberEntity savedMemberEntity = emailMemberRepository.save(memberEntity);

        return savedMemberEntity.toDomain();
    }

    @Transactional(readOnly = true)
    public EmailMember login(MemberRequestDto memberRequestDto) {
        EmailMemberEntity memberEntity = emailMemberRepository.findByEmail(memberRequestDto.email())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 멤버입니다."));

        EmailMember member = memberEntity.toDomain();

        if (!member.validatePlainPassword(memberRequestDto.password())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return member;
    }
}
