package librarymanagement.repository;

import librarymanagement.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByIsbn(String isbn);
    @Query("""
        SELECT b
        FROM Book b
        WHERE (:title IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%')))
        AND (:isbn IS NULL OR b.isbn = :isbn)
        AND (:authorId IS NULL OR b.author.id = :authorId)
        AND (:startYear IS NULL OR b.publishedYear >= :startYear)
        AND (:endYear IS NULL OR b.publishedYear <= :endYear)
        """)
        Page<Book> searchBooks(
                @Param("title") String title,
                @Param("isbn") String isbn,
                @Param("authorId") Long authorId,
                @Param("startYear") Integer startYear,
                @Param("endYear") Integer endYear,
                Pageable pageable
        );
}
