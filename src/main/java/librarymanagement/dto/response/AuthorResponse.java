package librarymanagement.dto.response;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthorResponse {

    @Schema(example = "1")
    private Long id;
    @Schema(example = "Jane")
    private String firstName;
    @Schema(example = "Austen")
    private String lastName;
    @Schema(example = "jane.austen@example.com")
    private String email;

}