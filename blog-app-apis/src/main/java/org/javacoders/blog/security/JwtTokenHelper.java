package org.javacoders.blog.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenHelper {
	
	public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;
	
	private String secret = "jwtTokenKey";
	
	// retrieve username from jwt token
	public String getUsernameFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}
	
	// retrieve expiration date from jwt token
	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}
	
	public <T> T getClaimFromToken(String token, Function<Claims, T> claimResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimResolver.apply(claims);
	}
	
	// for retrieving any information from token we will need a secret key
	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJwt(token).getBody();
	}
	
	// check if the token has expired
	private boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}
	
	// generate token for user
	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		String token = doGenerateToken(claims, userDetails.getUsername());
		return token;
	}
	
	// while creating a token - 
	// 1. Define claims of the token, like Issuer, Expiration, Subject, and the ID
	// 2. Sign the jwt using the HS512 algorithm and secret key
	// 3. According to JWS compact Serialization(https://tools.ietf.org/html/draft-ietf-jose-j
	//    compaction  of the JWT to a URL-safe string
	@SuppressWarnings("deprecation")
	private String doGenerateToken(Map<String, Object> claims, String subject) {
		 String token = Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 100))
				.signWith(SignatureAlgorithm.HS512, secret).compact();
		 return token;
	}
	
	// validate token
	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = getUsernameFromToken(token);
		return (username.equals(userDetails.getUsername()) && isTokenExpired(token));
	}
}
















