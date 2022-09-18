package com.blog.app;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.blog.app.entities.Role;
import com.blog.app.repositories.RoleRepo;
import com.blog.app.utils.AppConstants;

@SpringBootApplication
public class BlogApplication implements CommandLineRunner {

	@Autowired
	private RoleRepo roleRepo;

	public static void main(String[] args) {
		SpringApplication.run(BlogApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		try {

			Role role1=new Role();
			role1.setId(AppConstants.ADMIN_USER);
			role1.setName("ROLE_ADMIN");
			
		
			
			Role role2=new Role();
			role2.setId(AppConstants.NORMAL_USER);
			role2.setName("ROLE_NORMAL");
			
			List<Role> roles=Arrays.asList(role1,role2);
			this.roleRepo.saveAll(roles);
			
			roles.forEach(r->System.out.println(r.getName()));
			
			
		} catch (Exception e) {
			System.out.println("Exception occured in run method");
		}

	}

}
