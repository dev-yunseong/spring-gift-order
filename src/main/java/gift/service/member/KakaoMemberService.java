package gift.service.member;

import gift.domain.member.KakaoMember;
import gift.entity.member.KakaoMemberEntity;
import gift.repository.member.KakaoMemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class KakaoMemberService {

    private final KakaoMemberRepository kakaoMemberRepository;

    public KakaoMemberService(KakaoMemberRepository kakaoMemberRepository) {
        this.kakaoMemberRepository = kakaoMemberRepository;
    }

    public KakaoMember registerOrLogin(KakaoMember kakaoMember) {
        if (kakaoMemberRepository.findByKakaoId(kakaoMember.getKakaoId()).isPresent()) {
            return login(kakaoMember);
        }
        return creatMember(kakaoMember);
    }

    private KakaoMember creatMember(KakaoMember kakaoMember) {
        KakaoMemberEntity kakaoMemberEntity = new KakaoMemberEntity(kakaoMember);
        KakaoMemberEntity savedKakaoMemberEntity = kakaoMemberRepository.save(kakaoMemberEntity);

        return savedKakaoMemberEntity.toDomain();
    }

    @Transactional(readOnly = true)
    protected KakaoMember login(KakaoMember kakaoMember) {
        KakaoMemberEntity savedKakaoMemberEntity = kakaoMemberRepository.findByKakaoId(kakaoMember.getKakaoId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 멤버입니다."));

        return savedKakaoMemberEntity.toDomain();
    }
}
