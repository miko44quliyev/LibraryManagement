package librarymanagement.dto.response;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookResponse {

    @Schema(example = "1")
    private Long id;
    @Schema(example = "9780141439518")
    private String isbn;
    @Schema(example = "Pride and Prejudice")
    private String title;
    @Schema(example = "1813")
    private Integer publishedYear;
    @Schema(example = "5")
    private Integer totalCopies;
    @Schema(example = "1")
    private Long authorId;
    @Schema(example = "Jane Austen")
    private String authorName;

}