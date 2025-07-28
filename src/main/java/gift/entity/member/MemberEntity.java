package gift.entity.member;

import gift.domain.member.Member;
import gift.entity.WishEntity;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "members")
@Inheritance(strategy = InheritanceType.JOINED)
public class MemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    public Long getId() {
        return id;
    }

    @OneToMany(mappedBy = "memberEntity")
    protected List<WishEntity> wishEntities;

    public List<WishEntity> getWishEntities() {
        return wishEntities;
    }


    public Member toDomain() {
        return new Member(id);
    }

    protected MemberEntity() {}

    public MemberEntity(Member member) {
        this(member.getId());
    }

    public MemberEntity(Long id) {
        this.id = id;
    }
}
