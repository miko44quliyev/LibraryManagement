package librarymanagement.dto.response;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberResponse {

    @Schema(example = "1")
    private Long id;
    @Schema(example = "John")
    private String firstName;
    @Schema(example = "Doe")
    private String lastName;
    @Schema(example = "john.doe@example.com")
    private String email;
    @Schema(example = "555-0100")
    private String phoneNumber;
    @Schema(example = "2026-07-18")
    private LocalDate membershipDate;

}