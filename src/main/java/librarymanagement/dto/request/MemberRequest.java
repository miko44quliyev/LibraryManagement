package librarymanagement.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberRequest {

    @NotBlank
    @Schema(example = "John")
    private String firstName;

    @NotBlank
    @Schema(example = "Doe")
    private String lastName;

    @NotBlank
    @Email
    @Schema(example = "john.doe@example.com")
    private String email;

    @NotBlank
    @Schema(example = "555-0100")
    private String phoneNumber;

    @NotNull
    @Schema(example = "2026-07-18")
    private LocalDate membershipDate;

}