package librarymanagement.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookRequest {

    @NotBlank
    @Schema(example = "9780141439518")
    private String isbn;

    @NotBlank
    @Schema(example = "Pride and Prejudice")
    private String title;

    @NotNull
    @Schema(example = "1813")
    private Integer publishedYear;

    @NotNull
    @Positive
    @Schema(example = "5")
    private Integer totalCopies;

    @NotNull
    @Schema(example = "1")
    private Long authorId;

}