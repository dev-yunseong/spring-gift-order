package gift.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import gift.domain.member.KakaoMember;

import java.time.LocalDateTime;

public class KakaoUserInfoResponseDto {
    private Long id;

    public Long getId() {
        return id;
    }

    @JsonProperty("connected_at")
    private LocalDateTime connectedAt;

    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    public KakaoAccount getKakaoAccount() {
        return kakaoAccount;
    }

    public KakaoUserInfoResponseDto(Long id, String nickname, String profileImageUrl) {
        this.id = id;
        this.kakaoAccount = new KakaoAccount();
        this.kakaoAccount.profile = new KakaoAccount.Profile();
        this.kakaoAccount.profile.profileImageUrl = profileImageUrl;
        this.kakaoAccount.profile.nickname = nickname;
    }


    public static class KakaoAccount {

        private Profile profile;

        public Profile getProfile() {
            return profile;
        }

        public static class Profile {
            private String nickname;

            public String getNickname() {
                return nickname;
            }

            @JsonProperty("thumbnail_image_url")
            private String thumbnailImageUrl;

            @JsonProperty("profile_image_url")
            private String profileImageUrl;

            public String getProfileImageUrl() {
                return profileImageUrl;
            }
        }
    }

    public KakaoMember toDomain() {
        return new KakaoMember(
                null,
                id,
                kakaoAccount.profile.nickname,
                kakaoAccount.profile.profileImageUrl);
    }
}
