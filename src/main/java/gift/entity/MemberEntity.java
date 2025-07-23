package gift.entity;

import gift.domain.Member;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "members")
public class MemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String email;

    @Column(nullable = false, length = 60)
    private String passwordHash;

    @OneToMany(mappedBy = "memberEntity")
    private List<WishEntity> wishEntities;

    public List<WishEntity> getWishEntities() {
        return wishEntities;
    }

    public Member toDomain() {
        return new Member(id, email, passwordHash);
    }

    protected MemberEntity() {}

    public MemberEntity(Member member) {
        this(member.getId(), member.getEmail(), member.getPasswordHash());
    }

    public MemberEntity(Long id, String email, String passwordHash) {
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
    }
}
