package gift.service.member;

import gift.domain.member.SocialMember;
import gift.entity.member.SocialMemberEntity;
import gift.repository.member.SocialMemberRepository;
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
public class SocialMemberServiceTest {

    @Mock
    private SocialMemberRepository socialMemberRepository;

    private SocialMemberService socialMemberService;

    @BeforeEach
    void initKakaoMemberService() {
        socialMemberService = new SocialMemberService(socialMemberRepository);
    }

    @Test
    void 회원가입_테스트() {
        SocialMember socialMember = new SocialMember(null, 123L, SocialMember.Provider.KAKAO, "name", "path");
        SocialMemberEntity kakaoMemberEntity = new SocialMemberEntity(1L, 123L, SocialMember.Provider.KAKAO,"name", "path");
        given(socialMemberRepository.findByProviderIdAndProvider(eq(123L), eq(SocialMember.Provider.KAKAO))).willReturn(Optional.empty());
        given(socialMemberRepository.save(any())).willReturn(kakaoMemberEntity);

        SocialMember savedSocialMember = socialMemberService.registerOrLogin(socialMember);

        then(socialMemberRepository).should().save(any());
        assertThat(savedSocialMember.getProviderId()).isEqualTo(123L);
    }

    @Test
    void 로그인_테스트() {
        SocialMember socialMember = new SocialMember(null, 123L, SocialMember.Provider.KAKAO, "name", "path");
        SocialMemberEntity kakaoMemberEntity = new SocialMemberEntity(1L, 123L, SocialMember.Provider.KAKAO, "name", "path");
        given(socialMemberRepository.findByProviderIdAndProvider(eq(123L), eq(SocialMember.Provider.KAKAO))).willReturn(Optional.of(kakaoMemberEntity));

        SocialMember savedSocialMember = socialMemberService.registerOrLogin(socialMember);

        then(socialMemberRepository).should(never()).save(any());
        assertThat(savedSocialMember.getProviderId()).isEqualTo(123L);
    }
}
