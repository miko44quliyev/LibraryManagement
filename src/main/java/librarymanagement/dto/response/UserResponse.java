package librarymanagement.dto.response;

import librarymanagement.entity.UserRole;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private UUID id;
    private String fullName;
    private String email;
    private UserRole role;
    private LocalDateTime createdAt;
}