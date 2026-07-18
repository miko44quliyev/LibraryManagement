package librarymanagement.service;

import librarymanagement.dto.request.AuthorRequest;
import librarymanagement.dto.response.AuthorResponse;
import librarymanagement.entity.Author;
import librarymanagement.exception.ResourceNotFoundException;
import librarymanagement.mapper.AuthorMapper;
import librarymanagement.repository.AuthorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private AuthorMapper authorMapper;

    @InjectMocks
    private AuthorService authorService;

    @Test
    void createShouldSaveAuthor() {
        AuthorRequest request = AuthorRequest.builder()
                .firstName("Jane")
                .lastName("Austen")
                .email("jane.austen@example.com")
                .build();
        Author author = Author.builder()
                .firstName("Jane")
                .lastName("Austen")
                .email("jane.austen@example.com")
                .build();
        Author savedAuthor = Author.builder()
                .id(1L)
                .firstName("Jane")
                .lastName("Austen")
                .email("jane.austen@example.com")
                .build();
        AuthorResponse response = AuthorResponse.builder()
                .id(1L)
                .firstName("Jane")
                .lastName("Austen")
                .email("jane.austen@example.com")
                .build();

        when(authorMapper.toEntity(request)).thenReturn(author);
        when(authorRepository.save(author)).thenReturn(savedAuthor);
        when(authorMapper.toResponse(savedAuthor)).thenReturn(response);

        AuthorResponse result = authorService.create(request);

        assertThat(result).isEqualTo(response);
        verify(authorRepository).save(author);
    }

    @Test
    void getAllShouldReturnPagedAuthors() {
        Author author = Author.builder()
                .id(1L)
                .firstName("Jane")
                .lastName("Austen")
                .email("jane.austen@example.com")
                .build();
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Author> page = new PageImpl<>(List.of(author), pageable, 1);
        AuthorResponse response = AuthorResponse.builder()
                .id(1L)
                .firstName("Jane")
                .lastName("Austen")
                .email("jane.austen@example.com")
                .build();

        when(authorRepository.findAll(pageable)).thenReturn(page);
        when(authorMapper.toResponse(author)).thenReturn(response);

        Page<AuthorResponse> result = authorService.getAll(pageable);

        assertThat(result).hasSize(1);
        assertThat(result.getContent()).containsExactly(response);
    }

    @Test
    void getByIdShouldReturnAuthor() {
        Author author = Author.builder()
                .id(1L)
                .firstName("Jane")
                .lastName("Austen")
                .email("jane.austen@example.com")
                .build();
        AuthorResponse response = AuthorResponse.builder()
                .id(1L)
                .firstName("Jane")
                .lastName("Austen")
                .email("jane.austen@example.com")
                .build();

        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(authorMapper.toResponse(author)).thenReturn(response);

        AuthorResponse result = authorService.getById(1L);

        assertThat(result).isEqualTo(response);
    }

    @Test
    void getByIdShouldThrowWhenMissing() {
        when(authorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authorService.getById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Author not found");
    }

    @Test
    void updateShouldPersistChanges() {
        AuthorRequest request = AuthorRequest.builder()
                .firstName("Jane")
                .lastName("Austen")
                .email("jane.austen@example.com")
                .build();
        Author existing = Author.builder()
                .id(1L)
                .firstName("Old")
                .lastName("Name")
                .email("old@example.com")
                .build();
        Author updated = Author.builder()
                .id(1L)
                .firstName("Jane")
                .lastName("Austen")
                .email("jane.austen@example.com")
                .build();
        AuthorResponse response = AuthorResponse.builder()
                .id(1L)
                .firstName("Jane")
                .lastName("Austen")
                .email("jane.austen@example.com")
                .build();

        when(authorRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(authorRepository.save(existing)).thenReturn(updated);
        when(authorMapper.toResponse(updated)).thenReturn(response);

        AuthorResponse result = authorService.update(1L, request);

        assertThat(result).isEqualTo(response);
        assertThat(existing.getFirstName()).isEqualTo("Jane");
        assertThat(existing.getLastName()).isEqualTo("Austen");
        assertThat(existing.getEmail()).isEqualTo("jane.austen@example.com");
    }

    @Test
    void deleteShouldRemoveAuthor() {
        Author existing = Author.builder()
                .id(1L)
                .firstName("Jane")
                .lastName("Austen")
                .email("jane.austen@example.com")
                .build();

        when(authorRepository.findById(1L)).thenReturn(Optional.of(existing));

        authorService.delete(1L);

        verify(authorRepository).delete(existing);
    }

    @Test
    void deleteShouldThrowWhenMissing() {
        when(authorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authorService.delete(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Author not found");

        verify(authorRepository, never()).delete(any());
    }
}
