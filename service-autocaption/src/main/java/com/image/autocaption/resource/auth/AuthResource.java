package com.image.autocaption.resource.auth;

import com.image.autocaption.model.AuthRequestDto;
import com.image.autocaption.model.AuthResponseDto;
import com.image.autocaption.model.TokenRefreshRequestDto;
import com.image.autocaption.security.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.image.autocaption.security.JwtUtil.SECRET_KEY;

/**
 * The resource that exposes end points for generating JWT tokens for user authorization.
 */
@RestController
@RequestMapping("/auth")
public class AuthResource {

    private final AuthenticationManager authenticationManager;

    private final JwtUtil jwtUtil;

    private final UserDetailsService userDetailsService;

    public AuthResource(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    /**
     * The endpoint that returns a pair of access token & refresh token, in exchange for registered client credentials
     *
     * @param request dto containing username and password credentials for user authentication
     * @return a pair of access token & refresh token, once the user gets authenticated successfully
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> authenticate(@RequestBody AuthRequestDto request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        final String accessToken = jwtUtil.generateAccessToken(userDetails);
        final String refreshToken = jwtUtil.generateRefreshToken(userDetails);

        return ResponseEntity.ok(new AuthResponseDto(accessToken, refreshToken));
    }

    /**
     * The endpoint that refreshes an expired access token after successful validation of a refresh token.
     * New refresh token is generated as well to support token rotation
     *
     * @param request the refresh token which once verified can generate a new access token
     * @return pair of fresh generated access token & refresh token, as long as the input refresh token stands valid.
     */
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDto> refresh(@RequestBody TokenRefreshRequestDto request) {
        String refreshToken = request.getRefreshToken();

        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(refreshToken)
                    .getBody();

            String username = claims.getSubject();

            UserDetails user = userDetailsService.loadUserByUsername(username);
            String newAccessToken = jwtUtil.generateAccessToken(user);
            String newRefreshToken = jwtUtil.generateRefreshToken(user);
            return ResponseEntity.ok(new AuthResponseDto(newAccessToken, newRefreshToken));
        } catch (JwtException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

}

