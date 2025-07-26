package gift.service.member;

import gift.domain.member.KakaoMember;
import gift.entity.member.KakaoMemberEntity;
import gift.repository.member.KakaoMemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class KakaoMemberService {

    private final KakaoMemberRepository kakaoMemberRepository;

    public KakaoMemberService(KakaoMemberRepository kakaoMemberRepository) {
        this.kakaoMemberRepository = kakaoMemberRepository;
    }

    public KakaoMember registerOrLogin(KakaoMember kakaoMember) {
        Optional<KakaoMemberEntity> optionalKakaoMemberEntity = kakaoMemberRepository.findByKakaoId(kakaoMember.getKakaoId());
        if (optionalKakaoMemberEntity.isEmpty()) {
            return creatMember(kakaoMember);
        }
        return optionalKakaoMemberEntity.get().toDomain();
    }

    private KakaoMember creatMember(KakaoMember kakaoMember) {
        KakaoMemberEntity kakaoMemberEntity = new KakaoMemberEntity(kakaoMember);
        KakaoMemberEntity savedKakaoMemberEntity = kakaoMemberRepository.save(kakaoMemberEntity);

        return savedKakaoMemberEntity.toDomain();
    }
}
