package com.blog.app.payloads;

import lombok.Data;

@Data
public class JwtAuthRequest {

	private String email;
	private String password;
}
