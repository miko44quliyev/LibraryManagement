package librarymanagement.repository;

import librarymanagement.entity.Author;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    Optional<Author> findByEmail(String email);
    @Query("""
    SELECT a
    FROM Author a
    WHERE (:firstName IS NULL OR LOWER(a.firstName) LIKE LOWER(CONCAT('%', :firstName, '%')))
      AND (:lastName IS NULL OR LOWER(a.lastName) LIKE LOWER(CONCAT('%', :lastName, '%')))
      AND (:email IS NULL OR LOWER(a.email) LIKE LOWER(CONCAT('%', :email, '%')))
    """)
    Page<Author> searchAuthors(
            @Param("firstName") String firstName,
            @Param("lastName") String lastName,
            @Param("email") String email,
            Pageable pageable
    );

    @Query("""
    SELECT a
    FROM Author a
    JOIN a.books b
    WHERE b.publishedYear >= :year
    GROUP BY a
    """)
    Page<Author> findAuthorsWithBooksPublishedAfter(
            @Param("year") Integer year,
            Pageable pageable
    );
    @EntityGraph(attributePaths = {"books"})
    Optional<Author> findWithBooksById(Long id);
}
