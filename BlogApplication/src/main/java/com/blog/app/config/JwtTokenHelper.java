package com.blog.app.config;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenHelper {

	public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;

	@Value("${jwtSecretKey}")
	private String secretKey; // read from config server

	// retrieve username from jwt token
	public String getUsernameFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}

	// retrieve expiration Date from token
	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	public <T> 	T getClaimFromToken(String token,Function<Claims,T> claimsResolver) {
		final Claims claims=getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}
	//for retrieving any claim from token we need secret key
	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
	}
	
	//check if token is expired
	private Boolean isTokenExpired(String token) {
		final Date expiration=getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}
	
	//generate Token for user
	public String generateTokenForUser(UserDetails userDetails) {
		Map<String,Object> claims=new HashMap<>();
		return doGenerateToken(claims,userDetails.getUsername()); 
	}

	//while creating token
	//1)Define claims of token like issuer,expiration,subject and the id
	//2)Signing the jwt token using HS512 and secret key.
	//3)According to JWS compact serialization,compaction of JWT to url safe string.
	private String doGenerateToken(Map<String,Object> claims,String subject) {
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(subject)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis()+JWT_TOKEN_VALIDITY*100 ))
				.signWith(SignatureAlgorithm.HS512,secretKey).compact();
	}
	//validate token
	public Boolean validateToken(String token,UserDetails userDetails) {
		final String username=getUsernameFromToken(token);
		return (null!=username && username.equals(userDetails.getUsername())&& !isTokenExpired(token));
	}
}
