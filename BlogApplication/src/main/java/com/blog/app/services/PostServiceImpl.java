package com.blog.app.services;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.blog.app.entities.Category;
import com.blog.app.entities.Post;
import com.blog.app.entities.User;
import com.blog.app.exceptions.ResourceNotFoundException;
import com.blog.app.payloads.PostDto;
import com.blog.app.payloads.PostResponse;
import com.blog.app.repositories.CategoryRepo;
import com.blog.app.repositories.PostRepo;
import com.blog.app.repositories.UserRepo;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PostServiceImpl implements PostService {

	@Autowired
	private PostRepo postRepo;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private CategoryRepo categoryRepo;

	@Autowired
	private UserRepo userRepo;

	@Override
	public PostDto createPost(PostDto postDto, Integer postUserId, Integer categoryId) {

		log.info("Inside Post Service for creation of Post with Post id:"+postDto.getPostId()+" User id:"+postUserId+" Category id:"+categoryId);
		
		User user = this.userRepo.findById(postUserId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "User id", postUserId));
		Category category = this.categoryRepo.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "Category id", categoryId));

		Post post = this.modelMapper.map(postDto, Post.class);
		post.setImageName("default.png");
		post.setAddedDate(new Date());
		post.setUser(user);
		post.setCategory(category);

		Post savedPost = this.postRepo.save(post);

		return this.modelMapper.map(savedPost, PostDto.class);
	}

	@Override
	public PostDto updatePost(PostDto postDto, Integer postId) {
		log.info("Inside Post Service for updation of Post with Post id:"+postDto.getPostId());
		
		Post post = this.postRepo.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post", "Postid", postId));
		post.setTitle(postDto.getTitle());
		post.setContent(postDto.getContent());
		post.setImageName(postDto.getImageName());

		Post updatedPost = this.postRepo.save(post);
		return this.modelMapper.map(updatedPost, PostDto.class);
	}

	@Override
	public void deletePost(Integer postId) {
		log.info("Inside Post Service for deletion of Post with Post id:"+postId);
		
		Post post = this.postRepo.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post", "Postid", postId));
		this.postRepo.delete(post);
	}

	@Override
	public PostResponse getAllPosts(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
		log.info("Inside Post Service for fetching all posts");
		
		Sort sort = null;
		sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

		Page<Post> pagePost = this.postRepo.findAll(pageable);
		List<Post> postList = pagePost.getContent();

		List<PostDto> postDtoList = postList.stream().map((post) -> this.modelMapper.map(post, PostDto.class))
				.collect(Collectors.toList());

		PostResponse postResponse = new PostResponse();
		postResponse.setContent(postDtoList);
		postResponse.setPageNumber(pagePost.getNumber());
		postResponse.setPageSize(pagePost.getSize());
		postResponse.setTotalElements(pagePost.getTotalElements());
		postResponse.setTotalPages(pagePost.getTotalPages());

		return postResponse;
	}

	@Override
	public PostDto getPostById(Integer id) {
		log.info("Inside Post Service for fetching a post with id:"+id);
		
		Post post = this.postRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "Postid", id));
		;

		return this.modelMapper.map(post, PostDto.class);
	}

	@Override
	public List<PostDto> getPostByCategory(Integer categoryId) {
		log.info("Inside Post Service for fetching a post with Category id:"+categoryId);
		
		Category category = this.categoryRepo.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "Categoryid", categoryId));
		List<Post> posts = this.postRepo.findPostByCategory(category);
		List<PostDto> postDtoList = posts.stream().map((post) -> this.modelMapper.map(post, PostDto.class))
				.collect(Collectors.toList());

		return postDtoList;
	}

	@Override
	public List<PostDto> getPostByUser(Integer userId) {
		
		log.info("Inside Post Service for fetching a post with User id:"+userId);
		
		User user = this.userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "Userid", userId));
		List<Post> posts = this.postRepo.findPostByUser(user);
		List<PostDto> postDtoList = posts.stream().map((post) -> this.modelMapper.map(post, PostDto.class))
				.collect(Collectors.toList());

		return postDtoList;
	}

	@Override
	public List<PostDto> searchPosts(String keyword) {
		log.info("Inside Post Service for fetching a post with Keyword:"+keyword);
		
		List<Post> postList = this.postRepo.findByTitleContaining(keyword);
		List<PostDto> postDtoList = postList.stream().map((post) -> this.modelMapper.map(post, PostDto.class))
				.collect(Collectors.toList());

		return postDtoList;
	}

}
