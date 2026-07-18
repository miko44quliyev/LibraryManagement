package librarymanagement.controller;

import jakarta.validation.Valid;
import librarymanagement.dto.request.AuthorRequest;
import librarymanagement.dto.response.AuthorResponse;
import librarymanagement.service.AuthorService;
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
@RequestMapping("/api/v1/authors")
@RequiredArgsConstructor
@Tag(name = "Authors", description = "Author management endpoints")
public class AuthorController {

    private final AuthorService authorService;

    @PostMapping
    @Operation(summary = "Create an author")
    public ResponseEntity<AuthorResponse> create(@Valid @RequestBody AuthorRequest request) {
        AuthorResponse response = authorService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "List authors with pagination and sorting")
    public ResponseEntity<Page<AuthorResponse>> getAll(@PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(authorService.getAll(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an author by id")
    public ResponseEntity<AuthorResponse> getById(@PathVariable Long id) {
        AuthorResponse author = authorService.getById(id);
        return ResponseEntity.ok(author);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an author")
    public ResponseEntity<AuthorResponse> update(
            @PathVariable Long id,
            @Valid
            @RequestBody AuthorRequest request) {

        AuthorResponse updatedAuthor = authorService.update(id, request);
        return ResponseEntity.ok(updatedAuthor);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an author")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        authorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}