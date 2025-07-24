package gift.domain.member;

public class KakaoMember extends Member {

    private final Long kakaoId;

    public Long getKakaoId() {
        return kakaoId;
    }

    private final String nickname;

    public String getNickname() {
        return nickname;
    }

    private final String profileImage;

    public String getProfileImage() {
        return profileImage;
    }

    public KakaoMember(Long id, Long kakaoId, String nickname, String profileImage) {
        super(id);
        this.kakaoId = kakaoId;
        this.nickname = nickname;
        this.profileImage = profileImage;
    }
}