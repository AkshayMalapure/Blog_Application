package com.blog.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.blog.app.config.JwtTokenHelper;
import com.blog.app.exceptions.InvalidCredentialsException;
import com.blog.app.payloads.JwtAuthRequest;
import com.blog.app.payloads.JwtAuthResponse;
import com.blog.app.payloads.UserDto;
import com.blog.app.services.UserService;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {

	@Autowired
	private JwtTokenHelper tokenHelper;

	@Autowired
	private UserDetailsService userDetailService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserService userService;
	
	
	
	@PostMapping("/login")
	public ResponseEntity<JwtAuthResponse> createToken(@RequestBody JwtAuthRequest request) throws Exception {

		this.authenticate(request.getEmail(), request.getPassword());
		UserDetails userDetails = this.userDetailService.loadUserByUsername(request.getEmail());
		String token = this.tokenHelper.generateTokenForUser(userDetails);

		JwtAuthResponse response = new JwtAuthResponse();
		response.setToken(token);
		return new ResponseEntity<JwtAuthResponse>(response, HttpStatus.OK);

	}

	private void authenticate(String email, String password) throws Exception {
		UsernamePasswordAuthenticationToken userNamePwdAuthToken = new UsernamePasswordAuthenticationToken(email,
				password);

		try {
			this.authenticationManager.authenticate(userNamePwdAuthToken);
		} catch (BadCredentialsException e) {
			throw new InvalidCredentialsException("invalid username or password");
		}

	}
	
	@PostMapping("/register")
	public ResponseEntity<UserDto> registerUser(@RequestBody UserDto userDto){
		
		UserDto registerUser=this.userService.registerUser(userDto);
		return new ResponseEntity<UserDto>(registerUser,HttpStatus.CREATED);
		
	}
}
