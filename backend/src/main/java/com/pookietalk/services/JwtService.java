package com.pookietalk.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
// import io.jsonwebtoken.SignatureAlgorithm; // No longer needed for HS256 constant
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger; // Optional: For logging errors
import org.slf4j.LoggerFactory; // Optional: For logging errors
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey; // Use SecretKey for type safety with HMAC
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    // Optional: Logger for better error handling
    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

    @Value("${spring.security.jwt.secret}")
    private String secretKey; // Your secret key from properties (ensure it's Base64 encoded and secure)

    @Value("${spring.security.jwt.expiration}")
    private long jwtExpiration; // e.g., 86400000 = 1 day in milliseconds

    /**
     * Extracts the username (subject) from the JWT token.
     *
     * @param token The JWT token.
     * @return The username.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts a specific claim from the JWT token using a claims resolver function.
     *
     * @param token          The JWT token.
     * @param claimsResolver A function to extract the desired claim.
     * @param <T>            The type of the claim.
     * @return The extracted claim.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Generates a JWT token for the given UserDetails with no extra claims.
     *
     * @param userDetails The user details.
     * @return The generated JWT token.
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * Generates a JWT token for the given UserDetails with additional claims.
     * Uses the modern JJWT builder API.
     *
     * @param extraClaims Additional claims to include in the token.
     * @param userDetails The user details.
     * @return The generated JWT token.
     */
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        SecretKey key = getSignInKey(); // Get the signing key
        String token = Jwts.builder()
                .claims(extraClaims) // Use claims() instead of setClaims()
                .subject(userDetails.getUsername()) // Use subject() instead of setSubject()
                .issuedAt(new Date(System.currentTimeMillis())) // Use issuedAt() instead of setIssuedAt()
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration)) // Use expiration() instead of setExpiration()
                .signWith(key, Jwts.SIG.HS256) // Use signWith(SecretKey, Algorithm) and Jwts.SIG constants
                .compact();

        // Optional debug log — remove in production or use logger
        // logger.debug("Generated token for user: {}", userDetails.getUsername());
        // System.out.println("Generated token for user: " + userDetails.getUsername()); // Keep if needed for quick debug

        return token;
    }

    /**
     * Validates the JWT token against the UserDetails.
     * Checks if the username matches and the token is not expired.
     *
     * @param token       The JWT token.
     * @param userDetails The user details to validate against.
     * @return True if the token is valid, false otherwise.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            final String username = extractUsername(token);
            return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
        } catch (Exception e) {
            // Log the exception for debugging purposes
            logger.error("JWT validation error: {} - Token: {}", e.getMessage(), token);
            return false; // Token is invalid if any exception occurs during parsing/validation
        }
    }

    /**
     * Checks if the JWT token has expired.
     *
     * @param token The JWT token.
     * @return True if the token is expired, false otherwise.
     */
    private boolean isTokenExpired(String token) {
        try {
            return extractExpiration(token).before(new Date());
        } catch (Exception e) {
            // If expiration cannot be extracted, treat as invalid/expired
            logger.error("Could not extract expiration from token: {}", e.getMessage());
            return true;
        }
    }

    /**
     * Extracts the expiration date from the JWT token.
     *
     * @param token The JWT token.
     * @return The expiration date.
     * @throws io.jsonwebtoken.JwtException if parsing fails.
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts all claims from the JWT token using the modern JJWT parser API.
     * This method will throw exceptions if the token is invalid, expired, or malformed.
     *
     * @param token The JWT token.
     * @return The Claims object.
     * @throws io.jsonwebtoken.JwtException if parsing or verification fails.
     */
    private Claims extractAllClaims(String token) {
        SecretKey key = getSignInKey(); // Get the verification key
        return Jwts.parser() // Use the simpler parser() entry point
                .verifyWith(key) // Specify the key for verification
                .build()
                .parseSignedClaims(token) // Parse and verify the token
                .getPayload(); // Get the claims payload
    }

    /**
     * Generates the signing/verification key from the Base64 encoded secret.
     * Uses the modern JJWT Keys API.
     *
     * @return The SecretKey instance.
     */
    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Optional: Refreshes a JWT token. Extracts claims from the old token
     * and issues a new one with updated issuedAt and expiration dates.
     * Handles ExpiredJwtException specifically to allow refreshing expired tokens.
     *
     * @param token The expired or soon-to-expire JWT token.
     * @return A new JWT token with refreshed expiration.
     * @throws RuntimeException if the token cannot be refreshed (e.g., malformed, invalid signature).
     */
    public String refreshToken(String token) {
        SecretKey key = getSignInKey(); // Get the signing key
        Claims claims;
        try {
            // Try parsing normally first
            claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (io.jsonwebtoken.ExpiredJwtException eje) {
            // If token is expired, extract claims from the exception
            logger.warn("Token expired, attempting refresh based on claims. User: {}", eje.getClaims().getSubject());
            claims = eje.getClaims();
        } catch (Exception e) {
            // Handle other exceptions (e.g., malformed token, signature invalid) - cannot refresh
            logger.error("Could not refresh token due to parsing error: {}", e.getMessage());
            throw new RuntimeException("Could not refresh token: Invalid token", e);
        }

        // If claims were successfully extracted (either from valid or expired token)
        return Jwts.builder()
                .claims(claims) // Re-use existing claims (includes subject, roles, etc.)
                // .subject(claims.getSubject()) // Subject is already in claims map
                .issuedAt(new Date(System.currentTimeMillis())) // Set new issuedAt time
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration)) // Set new expiration time
                .signWith(key, Jwts.SIG.HS256) // Sign with the key and algorithm
                .compact();
    }
}
