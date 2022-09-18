package com.blog.app.payloads;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class UserDto {
	private int id;

	@NotEmpty
	@Size(min=4 ,message = "username must be of min 4 characters in length")
	private String name;

	@Email(message = "Email is in invalid format")
	private String email;

	@NotEmpty(message = "Password cannot be empty")
	@Size(min=3,max=10,message="password must be min of 3 chars and max of 10 chars")
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;

	@NotEmpty(message = "UserInformation/About cannot be empty")
	private String about;
	
	Set<RoleDto> roles = new HashSet<>();

}
