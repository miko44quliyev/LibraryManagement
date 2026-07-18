package librarymanagement.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookRequest {

    private String isbn;
    private String title;
    private Integer publishedYear;
    private Integer totalCopies;
    private Long authorId;

}