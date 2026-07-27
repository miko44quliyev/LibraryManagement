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
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(UserRole.USER)
                .build();

        userRepository.save(user);

        return buildAuthResponse(user);
    }

    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }

        return buildAuthResponse(user);
    }

    public AuthResponse refresh(RefreshRequest request) {

        DecodedJWT jwt = jwtTokenProvider.validateRefreshToken(request.getRefreshToken());

        User user = userRepository.findByEmail(jwt.getSubject())
                .orElseThrow(() -> new BadCredentialsException("User not found"));

        return buildAuthResponse(user);
    }

    private AuthResponse buildAuthResponse(User user) {

        String accessToken = jwtTokenProvider.generateAccessToken(
                user.getEmail(),
                user.getRole().name());

        String refreshToken = jwtTokenProvider.generateRefreshToken(
                user.getEmail());

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .build();
    }
}