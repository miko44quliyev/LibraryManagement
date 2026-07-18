package librarymanagement.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthorRequest {

    @NotBlank
    @Size(min = 2, max = 50)
    @Schema(example = "Jane")
    private String firstName;

    @NotBlank
    @Size(min = 2, max = 50)
    @Schema(example = "Austen")
    private String lastName;

    @NotBlank
    @Email
    @Schema(example = "jane.austen@example.com")
    private String email;

}