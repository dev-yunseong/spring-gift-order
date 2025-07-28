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

    public SocialMember(Long id, Long providerId, Provider provider, String nickname, String profileImage) {
        super(id);
        this.providerId = providerId;
        this.provider = provider;
        this.nickname = nickname;
        this.profileImage = profileImage;
    }

    public enum Provider {
        KAKAO
    }
}