package gift.service.member;

import gift.domain.member.KakaoMember;
import gift.entity.member.KakaoMemberEntity;
import gift.repository.member.KakaoMemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
public class KakaoMemberServiceTest {

    @Mock
    private KakaoMemberRepository kakaoMemberRepository;

    private KakaoMemberService kakaoMemberService;

    @BeforeEach
    void initKakaoMemberService() {
        kakaoMemberService = new KakaoMemberService(kakaoMemberRepository);
    }

    @Test
    void 회원가입_테스트() {
        KakaoMember kakaoMember = new KakaoMember(null, 123L, "name", "path");
        KakaoMemberEntity kakaoMemberEntity = new KakaoMemberEntity(1L, 123L, "name", "path");
        given(kakaoMemberRepository.findByKakaoId(eq(123L))).willReturn(Optional.empty());
        given(kakaoMemberRepository.save(any())).willReturn(kakaoMemberEntity);

        KakaoMember savedKakaoMember = kakaoMemberService.registerOrLogin(kakaoMember);

        then(kakaoMemberRepository).should().save(any());
        assertThat(savedKakaoMember.getKakaoId()).isEqualTo(123L);
    }

    @Test
    void 로그인_테스트() {
        KakaoMember kakaoMember = new KakaoMember(null, 123L, "name", "path");
        KakaoMemberEntity kakaoMemberEntity = new KakaoMemberEntity(1L, 123L, "name", "path");
        given(kakaoMemberRepository.findByKakaoId(any())).willReturn(Optional.of(kakaoMemberEntity));

        KakaoMember savedKakaoMember = kakaoMemberService.registerOrLogin(kakaoMember);

        then(kakaoMemberRepository).should(never()).save(any());
        assertThat(savedKakaoMember.getKakaoId()).isEqualTo(123L);
    }
}
