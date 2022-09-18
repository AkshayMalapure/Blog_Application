package com.blog.app.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.blog.app.entities.Category;
import com.blog.app.entities.Post;
import com.blog.app.entities.User;

public interface PostRepo extends JpaRepository<Post,Integer>{

	List<Post> findPostByUser(User user);
	List<Post> findPostByCategory(Category category);
	List<Post> findByTitleContaining(@Param("key") String key);
}
