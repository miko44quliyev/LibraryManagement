package librarymanagement.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "loans")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "book_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Member member;

    @Column(nullable = false)
    private LocalDate borrowDate;

    private LocalDate returnDate;

    @Column(nullable = false)
    private boolean returned;
}