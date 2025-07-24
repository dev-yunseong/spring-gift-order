package gift.repository;

import gift.domain.member.EmailMember;
import gift.entity.member.EmailMemberEntity;
import gift.entity.member.MemberEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class EmailMemberRepositoryTest {

    @Autowired
    private EmailMemberRepository emailMemberRepository;

    @Test
    void saveTest() {
        EmailMemberEntity memberEntity = new EmailMemberEntity(null,"memberSave@demo", "password");
        EmailMemberEntity actualMemberEntity = emailMemberRepository.save(memberEntity);

        assertAll(
                () -> assertThat(actualMemberEntity.toDomain().getId()).isNotNull(),
                () -> assertThat(actualMemberEntity.toDomain().getEmail()).isEqualTo(memberEntity.toDomain().getEmail()),
                () -> assertThat(actualMemberEntity.toDomain().getPasswordHash()).isEqualTo(memberEntity.toDomain().getPasswordHash())
        );
    }

    @Test
    void findByEmailTest() {
        String email = "memberFindByEmail@demo";
        EmailMemberEntity memberEntity = new EmailMemberEntity(null, email, "password");
        emailMemberRepository.save(memberEntity);

        Optional<EmailMemberEntity> optionalMemberEntity = emailMemberRepository.findByEmail(email);

        assertAll(
                () -> assertThat(optionalMemberEntity.isPresent()).isTrue(),
                () -> assertThat(optionalMemberEntity.get().toDomain().getEmail()).isEqualTo(email)
        );
    }

    @Test
    void findByIdTest() {
        String email = "memberFindByEmail@demo";
        EmailMemberEntity memberEntity = new EmailMemberEntity(null, email, "password");
        EmailMemberEntity savedMemberEntity = emailMemberRepository.save(memberEntity);

        Optional<EmailMemberEntity> optionalMemberEntity = emailMemberRepository.findById(savedMemberEntity.toDomain().getId());

        assertAll(
                () -> assertThat(optionalMemberEntity.isPresent()).isTrue(),
                () -> assertThat(optionalMemberEntity.get().toDomain().getEmail()).isEqualTo(email)
        );
    }
}

