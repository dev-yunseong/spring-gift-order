package gift.repository;

import gift.entity.member.EmailMemberEntity;
import gift.entity.member.KakaoMemberEntity;
import gift.entity.member.MemberEntity;
import gift.repository.member.CommonMemberRepository;
import gift.repository.member.EmailMemberRepository;
import gift.repository.member.KakaoMemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class CommonMemberRepositoryTest {

    @Autowired
    private EmailMemberRepository emailMemberRepository;

    @Autowired
    private KakaoMemberRepository kakaoMemberRepository;

    @Autowired
    private CommonMemberRepository commonMemberRepository;

    @Test
    void Email_Member_가져오기_테스트() {
        EmailMemberEntity emailMemberEntity =
                new EmailMemberEntity(null, "email@email", "password");
        EmailMemberEntity savedEmailMemberEntity = emailMemberRepository.save(emailMemberEntity);

        Optional<MemberEntity> member = commonMemberRepository.findById(savedEmailMemberEntity.toDomain().getId());

        assertThat(member.isPresent()).isTrue();
    }

    @Test
    void Kakao_Member_가져오기_테스트() {
        KakaoMemberEntity kakaoMemberEntity =
                new KakaoMemberEntity(null, 123L, "member", "path");
        KakaoMemberEntity savedKakaoMemberEntity = kakaoMemberRepository.save(kakaoMemberEntity);

        Optional<MemberEntity> member = commonMemberRepository.findById(savedKakaoMemberEntity.toDomain().getId());

        assertThat(member.isPresent()).isTrue();
    }
}
