package gift.dto;

import gift.domain.member.EmailMember;
import jakarta.validation.constraints.Email;

public record MemberRequestDto(
        @Email
        String email,
        String password
) {
        public EmailMember toDomain() {
                return new EmailMember(email, password);
        }
}
