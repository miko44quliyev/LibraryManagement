package librarymanagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Entity
@Table(name = "books")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String isbn;

    @NotBlank
    @Column(nullable = false)
    private String title;

    @Positive
    @Column(nullable = false)
    private Integer publishedYear;

    @Positive
    @Column(nullable = false)
    private Integer totalCopies;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Author author;
}