package librarymanagement.service;

import librarymanagement.dto.request.BookRequest;
import librarymanagement.dto.response.BookResponse;
import librarymanagement.entity.Author;
import librarymanagement.entity.Book;
import librarymanagement.mapper.BookMapper;
import librarymanagement.repository.AuthorRepository;
import librarymanagement.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final BookMapper bookMapper;

    public BookResponse create(BookRequest request) {

        Author author = authorRepository.findById(request.getAuthorId())
                .orElseThrow(() -> new RuntimeException("Author not found"));

        Book book = bookMapper.toEntity(request);
        book.setAuthor(author);

        Book savedBook = bookRepository.save(book);

        return bookMapper.toResponse(savedBook);
    }

    public List<BookResponse> getAll() {
        return bookMapper.toResponseList(bookRepository.findAll());
    }

    public BookResponse getById(Long id) {

        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        return bookMapper.toResponse(book);
    }

    public BookResponse update(Long id, BookRequest request) {

        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        Author author = authorRepository.findById(request.getAuthorId())
                .orElseThrow(() -> new RuntimeException("Author not found"));

        book.setIsbn(request.getIsbn());
        book.setTitle(request.getTitle());
        book.setPublishedYear(request.getPublishedYear());
        book.setTotalCopies(request.getTotalCopies());
        book.setAuthor(author);

        Book updatedBook = bookRepository.save(book);

        return bookMapper.toResponse(updatedBook);
    }

    public void delete(Long id) {

        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        bookRepository.delete(book);
    }
}