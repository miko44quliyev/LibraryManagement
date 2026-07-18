package librarymanagement.service;

import librarymanagement.dto.request.AuthorRequest;
import librarymanagement.dto.response.AuthorResponse;
import librarymanagement.entity.Author;
import librarymanagement.exception.ResourceNotFoundException;
import librarymanagement.mapper.AuthorMapper;
import librarymanagement.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    public AuthorResponse create(AuthorRequest request) {
        Author author = authorMapper.toEntity(request);
        Author savedAuthor = authorRepository.save(author);
        return authorMapper.toResponse(savedAuthor);
    }

    public Page<AuthorResponse> getAll(Pageable pageable) {
        return authorRepository.findAll(pageable).map(authorMapper::toResponse);
    }

    public AuthorResponse getById(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() ->  new ResourceNotFoundException("Author not found"));

        return authorMapper.toResponse(author);
    }

    public AuthorResponse update(Long id, AuthorRequest request) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found"));

        author.setFirstName(request.getFirstName());
        author.setLastName(request.getLastName());
        author.setEmail(request.getEmail());

        Author updatedAuthor = authorRepository.save(author);

        return authorMapper.toResponse(updatedAuthor);
    }

    public void delete(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found"));

        authorRepository.delete(author);
    }
}