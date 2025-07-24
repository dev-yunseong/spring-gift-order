package gift.domain.member;

import org.mindrot.jbcrypt.BCrypt;

public class EmailMember extends Member {

    private final String email;
    private final String passwordHash;

    public String getEmail() {
        return email;
    }

    public Long getId() {
        return id;
    }

    public boolean validatePlainPassword(String password) {
        return BCrypt.checkpw(password, passwordHash);
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public EmailMember(String email, String password) {
        super(null);
        this.email = email;
        this.passwordHash = BCrypt.hashpw(
                password,
                BCrypt.gensalt());
    }

    public EmailMember(Long id, String email, String passwordHash) {
        super(id);
        this.email = email;
        this.passwordHash = passwordHash;
    }
}
