package gift.entity.member;

import gift.domain.member.EmailMember;
import gift.domain.member.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "email_members")
public class EmailMemberEntity extends MemberEntity {

    @Column(nullable = false, unique = true, length = 30)
    private String email;

    public String getEmail() {
        return email;
    }

    @Column(nullable = false, length = 60)
    private String passwordHash;

    public EmailMember toDomain() {
        return new EmailMember(id, email, passwordHash);
    }

    protected EmailMemberEntity() {}

    public EmailMemberEntity(EmailMember member) {
        this(member.getId(), member.getEmail(), member.getPasswordHash());
    }

    public EmailMemberEntity(Long id, String email, String passwordHash) {
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
    }
}
