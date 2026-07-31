package librarymanagement.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BorrowRequest {

    @NotNull
    private Long bookId;

    @NotNull
    private Long memberId;
}