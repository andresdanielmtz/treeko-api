package com.autummata.treeko.security;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	private final SecretKey signingKey;
	private final long ttlSeconds;

	public JwtService(@Value("${security.jwt.secret}") String secret,
			@Value("${security.jwt.ttl-seconds:604800}") long ttlSeconds) {
		this.signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
		this.ttlSeconds = ttlSeconds;
	}

	public String generateToken(String username) {
		Instant now = Instant.now();
		Instant expiry = now.plusSeconds(ttlSeconds);
		return Jwts.builder()
				.subject(username)
				.issuedAt(Date.from(now))
				.expiration(Date.from(expiry))
				.signWith(signingKey)
				.compact();
	}

	public String extractUsername(String token) {
		return parseAllClaims(token).getSubject();
	}

	public boolean isValid(String token) {
		Claims claims = parseAllClaims(token);
		Date expiration = claims.getExpiration();
		return expiration != null && expiration.after(new Date());
	}

	private Claims parseAllClaims(String token) {
		return Jwts.parser()
				.verifyWith(signingKey)
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}
}
