package gift.service.member;

import gift.domain.member.SocialMember;
import gift.entity.member.SocialMemberEntity;
import gift.repository.member.SocialMemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class SocialMemberService {

    private final SocialMemberRepository socialMemberRepository;

    public SocialMemberService(SocialMemberRepository socialMemberRepository) {
        this.socialMemberRepository = socialMemberRepository;
    }

    public SocialMember registerOrLogin(SocialMember socialMember) {
        Optional<SocialMemberEntity> optionalKakaoMemberEntity = socialMemberRepository.findByProviderIdAndProvider(socialMember.getProviderId(), socialMember.getProvider());
        if (optionalKakaoMemberEntity.isEmpty()) {
            return creatMember(socialMember);
        }
        return optionalKakaoMemberEntity.get().toDomain();
    }

    private SocialMember creatMember(SocialMember socialMember) {
        SocialMemberEntity kakaoMemberEntity = new SocialMemberEntity(socialMember);
        SocialMemberEntity savedKakaoMemberEntity = socialMemberRepository.save(kakaoMemberEntity);

        return savedKakaoMemberEntity.toDomain();
    }
}
