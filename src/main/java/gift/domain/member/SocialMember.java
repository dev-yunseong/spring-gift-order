package gift.domain.member;

public class SocialMember extends Member {

    private final Long providerId;

    public Long getProviderId() {
        return providerId;
    }

    private final Provider provider;

    public Provider getProvider() {
        return provider;
    }

    private final String nickname;

    public String getNickname() {
        return nickname;
    }

    private final String profileImage;

    public String getProfileImage() {
        return profileImage;
    }

    public final String accessToken;

    public String getAccessToken() {
        return accessToken;
    }

    public final String refreshToken;

    public String getRefreshToken() {
        return refreshToken;
    }

    public SocialMember(Long id, Long providerId, Provider provider, String nickname, String profileImage, String accessToken, String refreshToken) {
        super(id);
        this.providerId = providerId;
        this.provider = provider;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public enum Provider {
        KAKAO
    }
}