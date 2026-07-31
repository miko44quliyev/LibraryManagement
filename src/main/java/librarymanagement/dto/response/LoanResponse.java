package librarymanagement.dto.response;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanResponse {

    private Long id;
    private Long bookId;
    private Long memberId;
    private LocalDate borrowDate;
    private LocalDate returnDate;
    private boolean returned;
}