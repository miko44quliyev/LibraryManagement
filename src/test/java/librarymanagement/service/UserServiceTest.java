package librarymanagement.service;

import librarymanagement.dto.request.UpdateUserRequest;
import librarymanagement.dto.response.UserResponse;
import librarymanagement.entity.User;
import librarymanagement.entity.UserRole;
import librarymanagement.exception.ResourceNotFoundException;
import librarymanagement.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;


    @Test
    void getUsersShouldReturnAllUsers() {

        User user = User.builder()
                .id(UUID.randomUUID())
                .fullName("John Doe")
                .email("john@test.com")
                .role(UserRole.USER)
                .build();


        when(userRepository.findAll())
                .thenReturn(List.of(user));


        List<UserResponse> result =
                userService.getUsers();


        assertThat(result)
                .hasSize(1);

        assertThat(result.get(0).getEmail())
                .isEqualTo("john@test.com");
    }


    @Test
    void getUserShouldReturnUser() {

        UUID id = UUID.randomUUID();


        User user = User.builder()
                .id(id)
                .fullName("John Doe")
                .email("john@test.com")
                .role(UserRole.USER)
                .build();


        when(userRepository.findById(id))
                .thenReturn(Optional.of(user));


        UserResponse result =
                userService.getUser(id);


        assertThat(result.getId())
                .isEqualTo(id);

        assertThat(result.getEmail())
                .isEqualTo("john@test.com");
    }


    @Test
    void getUserShouldThrowWhenMissing() {

        UUID id = UUID.randomUUID();


        when(userRepository.findById(id))
                .thenReturn(Optional.empty());


        assertThatThrownBy(() ->
                userService.getUser(id))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("User not found");
    }


    @Test
    void updateUserShouldSaveChanges() {

        UUID id = UUID.randomUUID();


        UpdateUserRequest request =
                UpdateUserRequest.builder()
                        .fullName("Updated Name")
                        .email("updated@test.com")
                        .build();


        User user = User.builder()
                .id(id)
                .fullName("Old Name")
                .email("old@test.com")
                .role(UserRole.USER)
                .build();


        when(userRepository.findById(id))
                .thenReturn(Optional.of(user));


        when(userRepository.save(user))
                .thenReturn(user);


        UserResponse result =
                userService.updateUser(id, request);


        assertThat(result.getFullName())
                .isEqualTo("Updated Name");

        assertThat(result.getEmail())
                .isEqualTo("updated@test.com");


        verify(userRepository)
                .save(user);
    }


    @Test
    void deleteUserShouldDeleteUser() {

        UUID id = UUID.randomUUID();


        User user = User.builder()
                .id(id)
                .fullName("John Doe")
                .email("john@test.com")
                .role(UserRole.USER)
                .build();


        when(userRepository.findById(id))
                .thenReturn(Optional.of(user));


        userService.deleteUser(id);


        verify(userRepository)
                .delete(user);
    }


    @Test
    void deleteUserShouldThrowWhenMissing() {

        UUID id = UUID.randomUUID();


        when(userRepository.findById(id))
                .thenReturn(Optional.empty());


        assertThatThrownBy(() ->
                userService.deleteUser(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("User not found");


        verify(userRepository, never())
                .delete(any());
    }


    @Test
    void getCurrentUserShouldReturnAuthenticatedUser() {

        SecurityContextHolder.getContext()
                .setAuthentication(
                        new UsernamePasswordAuthenticationToken(
                                "john@test.com",
                                null
                        )
                );


        User user = User.builder()
                .id(UUID.randomUUID())
                .fullName("John Doe")
                .email("john@test.com")
                .role(UserRole.USER)
                .build();


        when(userRepository.findByEmail("john@test.com"))
                .thenReturn(Optional.of(user));


        UserResponse result =
                userService.getCurrentUser();


        assertThat(result.getEmail())
                .isEqualTo("john@test.com");


        SecurityContextHolder.clearContext();
    }


    @Test
    void updateCurrentUserShouldSaveChanges() {

        SecurityContextHolder.getContext()
                .setAuthentication(
                        new UsernamePasswordAuthenticationToken(
                                "john@test.com",
                                null
                        )
                );


        UpdateUserRequest request =
                UpdateUserRequest.builder()
                        .fullName("New Name")
                        .email("new@test.com")
                        .build();


        User user = User.builder()
                .email("john@test.com")
                .fullName("Old Name")
                .role(UserRole.USER)
                .build();


        when(userRepository.findByEmail("john@test.com"))
                .thenReturn(Optional.of(user));


        when(userRepository.save(user))
                .thenReturn(user);


        UserResponse result =
                userService.updateCurrentUser(request);


        assertThat(result.getFullName())
                .isEqualTo("New Name");


        verify(userRepository)
                .save(user);


        SecurityContextHolder.clearContext();
    }
}