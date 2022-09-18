package com.blog.app.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.blog.app.entities.User;
import com.blog.app.exceptions.ResourceNotFoundException;
import com.blog.app.repositories.UserRepo;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CustomUserDetailService implements UserDetailsService{

	@Autowired
	private UserRepo userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	   
		log.info("Inside CustomUser Details Service fetcing user details with name:"+username);
		User user=this.userRepo.findUserByEmail(username).orElseThrow(()->new ResourceNotFoundException("User","Email id:"+username));
		System.out.println("email:"+user.getEmail());
		return user;
	}

	
}
