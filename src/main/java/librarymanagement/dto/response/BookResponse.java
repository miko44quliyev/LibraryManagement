package librarymanagement.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookResponse {

    private Long id;
    private String isbn;
    private String title;
    private Integer publishedYear;
    private Integer totalCopies;
    private Long authorId;
    private String authorName;

}