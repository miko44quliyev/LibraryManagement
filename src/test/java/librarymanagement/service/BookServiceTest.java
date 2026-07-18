package librarymanagement.service;

import librarymanagement.dto.request.BookRequest;
import librarymanagement.dto.response.BookResponse;
import librarymanagement.entity.Author;
import librarymanagement.entity.Book;
import librarymanagement.exception.ResourceNotFoundException;
import librarymanagement.mapper.BookMapper;
import librarymanagement.repository.AuthorRepository;
import librarymanagement.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookService bookService;

    @Test
    void createShouldSaveBookWithAuthor() {
        BookRequest request = BookRequest.builder()
                .isbn("9780141439518")
                .title("Pride and Prejudice")
                .publishedYear(1813)
                .totalCopies(5)
                .authorId(1L)
                .build();
        Author author = Author.builder()
                .id(1L)
                .firstName("Jane")
                .lastName("Austen")
                .email("jane.austen@example.com")
                .build();
        Book book = Book.builder()
                .isbn("9780141439518")
                .title("Pride and Prejudice")
                .publishedYear(1813)
                .totalCopies(5)
                .build();
        Book saved = Book.builder()
                .id(2L)
                .isbn("9780141439518")
                .title("Pride and Prejudice")
                .publishedYear(1813)
                .totalCopies(5)
                .author(author)
                .build();
        BookResponse response = BookResponse.builder()
                .id(2L)
                .isbn("9780141439518")
                .title("Pride and Prejudice")
                .publishedYear(1813)
                .totalCopies(5)
                .authorId(1L)
                .authorName("Jane Austen")
                .build();

        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(bookMapper.toEntity(request)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(saved);
        when(bookMapper.toResponse(saved)).thenReturn(response);

        BookResponse result = bookService.create(request);

        assertThat(result).isEqualTo(response);
        assertThat(book.getAuthor()).isEqualTo(author);
        verify(bookRepository).save(book);
    }

    @Test
    void getAllShouldReturnPagedBooks() {
        Author author = Author.builder()
                .id(1L)
                .firstName("Jane")
                .lastName("Austen")
                .email("jane.austen@example.com")
                .build();
        Book book = Book.builder()
                .id(2L)
                .isbn("9780141439518")
                .title("Pride and Prejudice")
                .publishedYear(1813)
                .totalCopies(5)
                .author(author)
                .build();
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Book> page = new PageImpl<>(List.of(book), pageable, 1);
        BookResponse response = BookResponse.builder()
                .id(2L)
                .isbn("9780141439518")
                .title("Pride and Prejudice")
                .publishedYear(1813)
                .totalCopies(5)
                .authorId(1L)
                .authorName("Jane Austen")
                .build();

        when(bookRepository.findAll(pageable)).thenReturn(page);
        when(bookMapper.toResponse(book)).thenReturn(response);

        Page<BookResponse> result = bookService.getAll(pageable);

        assertThat(result).hasSize(1);
        assertThat(result.getContent()).containsExactly(response);
    }

    @Test
    void getByIdShouldReturnBook() {
        Author author = Author.builder()
                .id(1L)
                .firstName("Jane")
                .lastName("Austen")
                .email("jane.austen@example.com")
                .build();
        Book book = Book.builder()
                .id(2L)
                .isbn("9780141439518")
                .title("Pride and Prejudice")
                .publishedYear(1813)
                .totalCopies(5)
                .author(author)
                .build();
        BookResponse response = BookResponse.builder()
                .id(2L)
                .isbn("9780141439518")
                .title("Pride and Prejudice")
                .publishedYear(1813)
                .totalCopies(5)
                .authorId(1L)
                .authorName("Jane Austen")
                .build();

        when(bookRepository.findById(2L)).thenReturn(Optional.of(book));
        when(bookMapper.toResponse(book)).thenReturn(response);

        BookResponse result = bookService.getById(2L);

        assertThat(result).isEqualTo(response);
    }

    @Test
    void getByIdShouldThrowWhenMissing() {
        when(bookRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.getById(2L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Book not found");
    }

    @Test
    void updateShouldPersistChanges() {
        BookRequest request = BookRequest.builder()
                .isbn("9780141439518")
                .title("Pride and Prejudice")
                .publishedYear(1813)
                .totalCopies(6)
                .authorId(1L)
                .build();
        Author author = Author.builder()
                .id(1L)
                .firstName("Jane")
                .lastName("Austen")
                .email("jane.austen@example.com")
                .build();
        Book existing = Book.builder()
                .id(2L)
                .isbn("0000000000")
                .title("Old Title")
                .publishedYear(1900)
                .totalCopies(1)
                .author(author)
                .build();
        Book updated = Book.builder()
                .id(2L)
                .isbn("9780141439518")
                .title("Pride and Prejudice")
                .publishedYear(1813)
                .totalCopies(6)
                .author(author)
                .build();
        BookResponse response = BookResponse.builder()
                .id(2L)
                .isbn("9780141439518")
                .title("Pride and Prejudice")
                .publishedYear(1813)
                .totalCopies(6)
                .authorId(1L)
                .authorName("Jane Austen")
                .build();

        when(bookRepository.findById(2L)).thenReturn(Optional.of(existing));
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(bookRepository.save(existing)).thenReturn(updated);
        when(bookMapper.toResponse(updated)).thenReturn(response);

        BookResponse result = bookService.update(2L, request);

        assertThat(result).isEqualTo(response);
        assertThat(existing.getIsbn()).isEqualTo("9780141439518");
        assertThat(existing.getTitle()).isEqualTo("Pride and Prejudice");
        assertThat(existing.getPublishedYear()).isEqualTo(1813);
        assertThat(existing.getTotalCopies()).isEqualTo(6);
        assertThat(existing.getAuthor()).isEqualTo(author);
    }

    @Test
    void deleteShouldRemoveBook() {
        Book existing = Book.builder()
                .id(2L)
                .isbn("9780141439518")
                .title("Pride and Prejudice")
                .publishedYear(1813)
                .totalCopies(5)
                .author(Author.builder().id(1L).firstName("Jane").lastName("Austen").email("jane.austen@example.com").build())
                .build();

        when(bookRepository.findById(2L)).thenReturn(Optional.of(existing));

        bookService.delete(2L);

        verify(bookRepository).delete(existing);
    }

    @Test
    void deleteShouldThrowWhenMissing() {
        when(bookRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.delete(2L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Book not found");

        verify(bookRepository, never()).delete(any());
    }
}
