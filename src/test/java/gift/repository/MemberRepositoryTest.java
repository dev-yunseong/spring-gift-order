package gift.repository;

import gift.entity.MemberEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void saveTest() {
        MemberEntity memberEntity = new MemberEntity(null,"memberSave@demo", "password");
        MemberEntity actualMemberEntity = memberRepository.save(memberEntity);

        assertAll(
                () -> assertThat(actualMemberEntity.toDomain().getId()).isNotNull(),
                () -> assertThat(actualMemberEntity.toDomain().getEmail()).isEqualTo(memberEntity.toDomain().getEmail()),
                () -> assertThat(actualMemberEntity.toDomain().getPasswordHash()).isEqualTo(memberEntity.toDomain().getPasswordHash())
        );
    }

    @Test
    void findByEmailTest() {
        String email = "memberFindByEmail@demo";
        MemberEntity memberEntity = new MemberEntity(null, email, "password");
        memberRepository.save(memberEntity);

        Optional<MemberEntity> optionalMemberEntity = memberRepository.findByEmail(email);

        assertAll(
                () -> assertThat(optionalMemberEntity.isPresent()).isTrue(),
                () -> assertThat(optionalMemberEntity.get().toDomain().getEmail()).isEqualTo(email)
        );
    }

    @Test
    void findByIdTest() {
        String email = "memberFindByEmail@demo";
        MemberEntity memberEntity = new MemberEntity(null, email, "password");
        MemberEntity savedMemberEntity = memberRepository.save(memberEntity);

        Optional<MemberEntity> optionalMemberEntity = memberRepository.findById(savedMemberEntity.toDomain().getId());

        assertAll(
                () -> assertThat(optionalMemberEntity.isPresent()).isTrue(),
                () -> assertThat(optionalMemberEntity.get().toDomain().getEmail()).isEqualTo(email)
        );
    }
}

