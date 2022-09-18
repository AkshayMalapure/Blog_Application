package com.blog.app.services;

import java.util.List;

import com.blog.app.payloads.PostDto;
import com.blog.app.payloads.PostResponse;

public interface PostService {
	
	PostDto createPost(PostDto postDto,Integer postUserId,Integer categoryId);
	PostDto updatePost(PostDto postDto,Integer postId);
	void deletePost(Integer postId);
	PostResponse getAllPosts(Integer pageNumber,Integer pageSize,String sortBy,String sortDir);
	PostDto getPostById(Integer id);
	List<PostDto> getPostByCategory(Integer categoryId);
	List<PostDto> getPostByUser(Integer userId);
	List<PostDto> searchPosts(String keyword);
	
}
