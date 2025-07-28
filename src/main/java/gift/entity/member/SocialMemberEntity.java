package gift.entity.member;

import gift.domain.member.SocialMember;
import jakarta.persistence.*;

@Entity
@Table(name = "social_members")
public class SocialMemberEntity extends MemberEntity {

    @Column(name = "provider_id", nullable = false)
    private Long providerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider", nullable = false)
    private SocialMember.Provider provider;

    @Column(nullable = false)
    private String nickname;

    @Column(name = "profile_image", nullable = false)
    private String profileImage;

    @Column(name = "access_token")
    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    }

    @Column(name = "refresh_token")
    private String refreshToken;

    protected SocialMemberEntity() {}

    public SocialMemberEntity(SocialMember socialMember) {
        this(
                socialMember.getId(),
                socialMember.getProviderId(),
                socialMember.getProvider(),
                socialMember.getNickname(),
                socialMember.getProfileImage(),
                socialMember.getAccessToken(),
                socialMember.getRefreshToken()
        );
    }

    public SocialMemberEntity(Long id, Long providerId, SocialMember.Provider provider, String nickname, String profileImage, String accessToken, String refreshToken) {
        super(id);
        this.provider = provider;
        this.providerId = providerId;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public SocialMember toDomain() {
        return new SocialMember(
                id, providerId, provider, nickname, profileImage, accessToken, refreshToken
        );
    }
}
