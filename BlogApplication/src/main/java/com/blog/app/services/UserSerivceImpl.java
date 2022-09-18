package com.blog.app.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.blog.app.entities.Role;
import com.blog.app.entities.User;
import com.blog.app.exceptions.ResourceNotFoundException;
import com.blog.app.payloads.UserDto;
import com.blog.app.repositories.RoleRepo;
import com.blog.app.repositories.UserRepo;

import lombok.extern.slf4j.Slf4j;
@Service
@Slf4j
public class UserSerivceImpl implements UserService {

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private RoleRepo roleRepo;
	
	@Override
	public UserDto createUser(UserDto userDto) {
		log.info("Inside User Service Creating User with Id:"+userDto.getId());
		User user = this.userRepo.save(dtoToUser(userDto));
		UserDto userdto = userToUserDto(user);
		return userdto;
	}

	@Override
	public UserDto updateUser(UserDto userDto, Integer userId) {
		log.info("Inside User Service Updating User with Id:"+userId);
		User user = this.userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
		user.setName(userDto.getName());
		user.setEmail(userDto.getEmail());
		user.setPassword(userDto.getPassword());
		user.setAbout(userDto.getAbout());

		User updatedUser = this.userRepo.save(user);
		UserDto userDto1 = userToUserDto(updatedUser);

		return userDto1;

	}

	@Override
	public UserDto getUserById(Integer userId) {
		log.info("Inside User Service Fetching  User Details with Id:"+userId);
		User user = this.userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
		return this.userToUserDto(user);
	}

	@Override
	public List<UserDto> getAllUsers() {
		log.info("Inside User Service Fetching  all User Details");
		List<User> userList = this.userRepo.findAll();
		List<UserDto> userDtoList = userList.stream().map(usr -> this.userToUserDto(usr)).collect(Collectors.toList());
		return userDtoList;
	}

	@Override
	public void deleteUser(Integer userId) {
		
		log.info("Inside User Service Deleting User with id:"+userId);
		
		User user = this.userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
		this.userRepo.delete(user);
	}

	private User dtoToUser(UserDto userDto) {
		User user = this.mapper.map(userDto, User.class);
		return user;
	}

	private UserDto userToUserDto(User user) {
		UserDto userDto = this.mapper.map(user,UserDto.class);
		return userDto;
	}

	@Override
	public UserDto registerUser(UserDto userDto) {
	
		log.info("Inside User Service Registering new  User with id:"+userDto.getId());
		
		User user=this.mapper.map(userDto,User.class);
		user.setPassword(this.passwordEncoder.encode(userDto.getPassword()));
		Role role=roleRepo.findById(502).get();
		user.getRoles().add(role);
		
	
		User saveUser=this.userRepo.save(user);
		
		return this.mapper.map(saveUser, UserDto.class);
	}
}
