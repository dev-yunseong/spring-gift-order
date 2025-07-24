package gift.entity.member;

import gift.domain.member.KakaoMember;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "kakao_members")
public class KakaoMemberEntity extends MemberEntity {

    @Column(name = "kakao_id", nullable = false)
    private Long kakaoId;

    @Column(nullable = false)
    private String nickname;

    @Column(name = "profile_image", nullable = false)
    private String profileImage;

    protected KakaoMemberEntity() {}

    public KakaoMemberEntity(KakaoMember kakaoMember) {
        this(
                kakaoMember.getId(),
                kakaoMember.getKakaoId(),
                kakaoMember.getNickname(),
                kakaoMember.getProfileImage()
        );
    }

    public KakaoMemberEntity(Long id, Long kakaoId, String nickname, String profileImage) {
        super(id);
        this.kakaoId = kakaoId;
        this.nickname = nickname;
        this.profileImage = profileImage;
    }

    public KakaoMember toDomain() {
        return new KakaoMember(
                id, kakaoId, nickname, profileImage
        );
    }
}
