package com.blog.app.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blog.app.payloads.ApiResponse;
import com.blog.app.payloads.UserDto;
import com.blog.app.services.UserService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/users")
@Slf4j
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping
	public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
		
		log.info("Inside User Controller creating User with id:"+userDto.getId());
		UserDto createUserDto = this.userService.createUser(userDto);
		return new ResponseEntity<>(createUserDto, HttpStatus.CREATED);
	}

	@PutMapping("/{userId}")
	public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto userDto, @PathVariable("userId") Integer userid) {
		
		log.info("Inside User Controller updating User with id:"+userid);
		
		UserDto updateUserDto = this.userService.updateUser(userDto, userid);
		return ResponseEntity.ok(updateUserDto);

	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{userId}")
	public ResponseEntity<ApiResponse> deleteUser(@PathVariable("userId") Integer id) {
     
		log.info("Inside User Controller Deleting User with id:"+id);
		this.userService.deleteUser(id);
		return new ResponseEntity<ApiResponse>(new ApiResponse("User deleted Successfully", true), HttpStatus.OK);

	}

	@GetMapping("/")
	public ResponseEntity<List<UserDto>> getAllUsers() {
		log.info("Inside User Controller feting all User Details");
		return ResponseEntity.ok(this.userService.getAllUsers());

	}

	@GetMapping("/{userId}")
	public ResponseEntity<UserDto> getUserDetailsById(@PathVariable("userId") Integer id) {
		log.info("Inside User Controller feting  User with id:"+id);
		return ResponseEntity.ok(this.userService.getUserById(id));
	}

}
