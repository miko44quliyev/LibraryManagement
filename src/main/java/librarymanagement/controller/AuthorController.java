package librarymanagement.controller;

import librarymanagement.dto.request.AuthorRequest;
import librarymanagement.dto.response.AuthorResponse;
import librarymanagement.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/authors")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @PostMapping
    public ResponseEntity<AuthorResponse> create(@RequestBody AuthorRequest request) {
        AuthorResponse response = authorService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<AuthorResponse>> getAll() {
        List<AuthorResponse> authors = authorService.getAll();
        return ResponseEntity.ok(authors);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorResponse> getById(@PathVariable Long id) {
        AuthorResponse author = authorService.getById(id);
        return ResponseEntity.ok(author);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthorResponse> update(
            @PathVariable Long id,
            @RequestBody AuthorRequest request) {

        AuthorResponse updatedAuthor = authorService.update(id, request);
        return ResponseEntity.ok(updatedAuthor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        authorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}