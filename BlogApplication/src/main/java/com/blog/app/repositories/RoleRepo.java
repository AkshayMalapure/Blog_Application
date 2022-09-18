package com.blog.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.blog.app.entities.Role;

@Repository
public interface RoleRepo extends JpaRepository<Role,Integer>{

}
