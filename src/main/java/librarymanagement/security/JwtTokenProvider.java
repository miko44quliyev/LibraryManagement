package librarymanagement.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
public class JwtTokenProvider {

    @Value("${jwt.access-secret}")
    private String accessSecret;

    @Value("${jwt.refresh-secret}")
    private String refreshSecret;

    @Value("${jwt.access-ttl-seconds}")
    private long accessTtl;

    @Value("${jwt.refresh-ttl-seconds}")
    private long refreshTtl;

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.audience}")
    private String audience;

    private Algorithm accessAlgorithm;
    private Algorithm refreshAlgorithm;

    private JWTVerifier accessVerifier;
    private JWTVerifier refreshVerifier;

    @PostConstruct
    public void init() {

        accessAlgorithm = Algorithm.HMAC256(accessSecret);
        refreshAlgorithm = Algorithm.HMAC256(refreshSecret);

        accessVerifier = JWT.require(accessAlgorithm)
                .withIssuer(issuer)
                .withAudience(audience)
                .withClaim("type", "access")
                .build();

        refreshVerifier = JWT.require(refreshAlgorithm)
                .withIssuer(issuer)
                .withAudience(audience)
                .withClaim("type", "refresh")
                .build();
    }

    public String generateAccessToken(String email, String role) {

        Instant now = Instant.now();

        return JWT.create()
                .withIssuer(issuer)
                .withAudience(audience)
                .withSubject(email)
                .withJWTId(UUID.randomUUID().toString())
                .withClaim("role", role)
                .withClaim("type", "access")
                .withIssuedAt(now)
                .withExpiresAt(now.plusSeconds(accessTtl))
                .sign(accessAlgorithm);
    }

    public String generateRefreshToken(String email) {

        Instant now = Instant.now();

        return JWT.create()
                .withIssuer(issuer)
                .withAudience(audience)
                .withSubject(email)
                .withJWTId(UUID.randomUUID().toString())
                .withClaim("type", "refresh")
                .withIssuedAt(now)
                .withExpiresAt(now.plusSeconds(refreshTtl))
                .sign(refreshAlgorithm);
    }



    public DecodedJWT validateAccessToken(String token) {
        return accessVerifier.verify(token);
    }

    public DecodedJWT validateRefreshToken(String token) {
        return refreshVerifier.verify(token);
    }
}