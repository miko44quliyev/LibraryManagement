package librarymanagement.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import librarymanagement.dto.auth.AuthResponse;
import librarymanagement.dto.auth.LoginRequest;
import librarymanagement.dto.auth.RefreshRequest;
import librarymanagement.dto.auth.RegisterRequest;
import librarymanagement.entity.User;
import librarymanagement.entity.UserRole;
import librarymanagement.repository.UserRepository;
import librarymanagement.security.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private DecodedJWT decodedJWT;

    @InjectMocks
    private AuthService authService;


    @Test
    void registerShouldCreateUser() {

        RegisterRequest request = RegisterRequest.builder()
                .fullName("John Doe")
                .email("john@test.com")
                .password("123456")
                .build();


        User user = User.builder()
                .email("john@test.com")
                .password("encoded")
                .role(UserRole.USER)
                .build();


        when(userRepository.existsByEmail("john@test.com"))
                .thenReturn(false);

        when(passwordEncoder.encode("123456"))
                .thenReturn("encoded");

        when(jwtTokenProvider.generateAccessToken(
                "john@test.com", "USER"))
                .thenReturn("access-token");

        when(jwtTokenProvider.generateRefreshToken(
                "john@test.com"))
                .thenReturn("refresh-token");


        AuthResponse result = authService.register(request);


        assertThat(result.getAccessToken())
                .isEqualTo("access-token");

        assertThat(result.getRefreshToken())
                .isEqualTo("refresh-token");

        verify(userRepository).save(any(User.class));
    }


    @Test
    void registerShouldThrowWhenEmailExists() {

        RegisterRequest request = RegisterRequest.builder()
                .email("john@test.com")
                .password("123456")
                .build();


        when(userRepository.existsByEmail("john@test.com"))
                .thenReturn(true);


        assertThatThrownBy(() ->
                authService.register(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Email already exists");


        verify(userRepository, never())
                .save(any());
    }


    @Test
    void loginShouldReturnToken() {

        LoginRequest request = LoginRequest.builder()
                .email("john@test.com")
                .password("123456")
                .build();


        User user = User.builder()
                .email("john@test.com")
                .password("encoded")
                .role(UserRole.USER)
                .build();


        when(userRepository.findByEmail("john@test.com"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(
                "123456",
                "encoded"))
                .thenReturn(true);


        when(jwtTokenProvider.generateAccessToken(
                "john@test.com",
                "USER"))
                .thenReturn("access-token");

        when(jwtTokenProvider.generateRefreshToken(
                "john@test.com"))
                .thenReturn("refresh-token");


        AuthResponse result = authService.login(request);


        assertThat(result.getAccessToken())
                .isEqualTo("access-token");

        assertThat(result.getRefreshToken())
                .isEqualTo("refresh-token");
    }


    @Test
    void loginShouldThrowWhenPasswordInvalid() {

        LoginRequest request = LoginRequest.builder()
                .email("john@test.com")
                .password("wrong")
                .build();


        User user = User.builder()
                .email("john@test.com")
                .password("encoded")
                .role(UserRole.USER)
                .build();


        when(userRepository.findByEmail("john@test.com"))
                .thenReturn(Optional.of(user));


        when(passwordEncoder.matches(
                "wrong",
                "encoded"))
                .thenReturn(false);


        assertThatThrownBy(() ->
                authService.login(request))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessage("Invalid email or password");
    }


    @Test
    void refreshShouldReturnNewTokens() {

        RefreshRequest request = RefreshRequest.builder()
                .refreshToken("refresh-token")
                .build();


        User user = User.builder()
                .email("john@test.com")
                .role(UserRole.USER)
                .build();


        when(jwtTokenProvider.validateRefreshToken(
                "refresh-token"))
                .thenReturn(decodedJWT);


        when(decodedJWT.getSubject())
                .thenReturn("john@test.com");


        when(userRepository.findByEmail("john@test.com"))
                .thenReturn(Optional.of(user));


        when(jwtTokenProvider.generateAccessToken(
                "john@test.com",
                "USER"))
                .thenReturn("access-token");


        when(jwtTokenProvider.generateRefreshToken(
                "john@test.com"))
                .thenReturn("refresh-token");


        AuthResponse result = authService.refresh(request);


        assertThat(result.getAccessToken())
                .isEqualTo("access-token");

        assertThat(result.getRefreshToken())
                .isEqualTo("refresh-token");
    }
}