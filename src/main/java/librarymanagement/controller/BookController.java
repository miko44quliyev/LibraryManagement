package librarymanagement.controller;

import jakarta.validation.Valid;
import librarymanagement.dto.request.BookRequest;
import librarymanagement.dto.response.BookResponse;
import librarymanagement.service.BookService;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
@Tag(name = "Books", description = "Book management endpoints")
public class BookController {

    private final BookService bookService;

    @PostMapping
    @Operation(summary = "Create a book")
    public ResponseEntity<BookResponse> create(@Valid @RequestBody BookRequest request) {
        BookResponse response = bookService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "List books with pagination and sorting")
    public ResponseEntity<Page<BookResponse>> getAll(@PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(bookService.getAll(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a book by id")
    public ResponseEntity<BookResponse> getById(@PathVariable Long id) {
        BookResponse book = bookService.getById(id);
        return ResponseEntity.ok(book);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a book")
    public ResponseEntity<BookResponse> update(
            @PathVariable Long id,
            @Valid
            @RequestBody BookRequest request) {

        BookResponse updatedBook = bookService.update(id, request);
        return ResponseEntity.ok(updatedBook);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a book")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bookService.delete(id);
        return ResponseEntity.noContent().build();
    }
}