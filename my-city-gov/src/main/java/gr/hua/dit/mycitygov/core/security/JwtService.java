package gr.hua.dit.mycitygov.core.security;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
    private final Key key;
    private final String issuer;
    private final String audience;
    private final Long ttlMinutes;

    public JwtService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.issuer}") String issuer,
            @Value("${jwt.audience}") String audience,
            @Value("${jwt.ttl-minutes}") Long ttlMinutes) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.issuer = issuer;
        this.audience = audience;
        this.ttlMinutes = ttlMinutes;
    }

    public String issue(String subject, Collection<String> roles) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(subject)
                .issuer(issuer)
                .setAudience(audience)
                .claim("roles", roles)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(Duration.ofMinutes(ttlMinutes))))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims parse(String token) {
        return Jwts.parser()
                .requireIssuer(issuer)
                .requireAudience(audience)
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
