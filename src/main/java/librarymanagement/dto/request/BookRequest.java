package librarymanagement.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookRequest {

    @NotBlank
    private String isbn;

    @NotBlank
    private String title;

    @NotNull
    private Integer publishedYear;

    @NotNull
    @Positive
    private Integer totalCopies;

    @NotNull
    private Long authorId;

}