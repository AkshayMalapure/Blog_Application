package com.blog.app.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.blog.app.config.JwtTokenHelper;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private JwtTokenHelper jwtTokenHelper;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
	
		log.info("Inside JwtAutenticationFilter doFilterInternal");
		
		//1:get token
		String requestToken=request.getHeader("Authorization");
		
		//Bearer shfjb4548
		System.out.println("req token:"+requestToken); 
		String username=null;
		
		String token=null;
		
		if(null!=requestToken && requestToken.startsWith("Bearer")) {
			token=requestToken.substring(7);
			
			try {
				username=this.jwtTokenHelper.getUsernameFromToken(token);	
			}catch(IllegalArgumentException ex) {
				log.error("Unable to get JWT token");
			}catch(ExpiredJwtException e) {
				log.error("Jwt token is expired");
			}catch(MalformedJwtException ex) {
				log.error("Invalid jwt exception");
			}
			
			
		}else {
			log.error("Jwt token does not begin with Bearer"); 
		}
		//once we get token,validate
		
		if(null!=username && SecurityContextHolder.getContext().getAuthentication()==null) {
			
			UserDetails userDetails=this.userDetailsService.loadUserByUsername(username);
			if(this.jwtTokenHelper.validateToken(token, userDetails)) {
				//autentication
				
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
				
				usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
				
				
				
				
			}else {
				log.error("invalid json token");
			}
			
		}else {
			log.error("username is null or context is not null"); 
		}
		filterChain.doFilter(request, response);
	}

}
