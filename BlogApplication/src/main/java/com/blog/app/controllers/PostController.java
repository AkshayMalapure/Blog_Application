package com.blog.app.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.hibernate.engine.jdbc.StreamUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.blog.app.payloads.ApiResponse;
import com.blog.app.payloads.PostDto;
import com.blog.app.payloads.PostResponse;
import com.blog.app.services.FileService;
import com.blog.app.services.PostService;
import com.blog.app.utils.AppConstants;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/")
@Slf4j
public class PostController {

	@Autowired
	private PostService postService;

	@Autowired
	private FileService fileService;

	@Value("${project.image}")
	private String path;

	@PostMapping("/user/{userId}/category/{categoryId}/posts")
	public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostDto postDto,
			@PathVariable("userId") Integer userId, @PathVariable("categoryId") Integer categoryId) {
        
		log.info("Inside Post Controller for creation of Post with Post id:"+postDto.getPostId()+" User id:"+userId+" Category id:"+categoryId);
		
		PostDto createdPost = this.postService.createPost(postDto, userId, categoryId);

		return new ResponseEntity<PostDto>(createdPost, HttpStatus.CREATED);
	}

	@GetMapping("/user/{userId}/posts")
	public ResponseEntity<List<PostDto>> getPostsByUserId(@PathVariable("userId") Integer id) {

		log.info("Inside Post Controller fetching Post Details with User id:"+id);
		
		List<PostDto> postList = this.postService.getPostByUser(id);
		return new ResponseEntity<List<PostDto>>(postList, HttpStatus.OK);
	}

	@GetMapping("/category/{categoryId}/posts")
	public ResponseEntity<List<PostDto>> getPostsByCategoryId(@PathVariable("categoryId") Integer id) {
		log.info("Inside Post Controller fetching Post Details with Category id:"+id); 
		
		List<PostDto> postList = this.postService.getPostByCategory(id);
		return new ResponseEntity<List<PostDto>>(postList, HttpStatus.OK);
	}

	@GetMapping("/posts")
	public ResponseEntity<PostResponse> getAllPosts(
			@RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir) {
		
		log.info("Inside Post Controller fetching all posts"); 

		PostResponse allPosts = this.postService.getAllPosts(pageNumber, pageSize, sortBy, sortDir);
		return new ResponseEntity<PostResponse>(allPosts, HttpStatus.OK);
	}

	@GetMapping("/posts/{postId}")
	public ResponseEntity<PostDto> getPostById(@PathVariable("postId") Integer id) {
		
		log.info("Inside Post Controller fetching post with id:"+id); 
		
		PostDto post = this.postService.getPostById(id);
		return new ResponseEntity<PostDto>(post, HttpStatus.OK);
	}

	@DeleteMapping("/posts/{postId}")
	public ResponseEntity<ApiResponse> deletePostById(@PathVariable("postId") Integer id) {
		
		log.info("Inside Post Controller deleting post with id:"+id); 
		
		this.postService.deletePost(id);
		return new ResponseEntity<ApiResponse>(new ApiResponse("Post deleted Successfully", true), HttpStatus.OK);
	}

	@PutMapping("/posts/{postId}")
	public ResponseEntity<PostDto> updatePost(@Valid @RequestBody PostDto postDto, @PathVariable("postId") Integer id) {
		log.info("Inside Post Controller updating post with id:"+id); 
		
		PostDto updatedPost = this.postService.updatePost(postDto, id);
		return new ResponseEntity<PostDto>(updatedPost, HttpStatus.OK);
	}

	@GetMapping("/posts/search/{keyword}")
	public ResponseEntity<List<PostDto>> searchPost(@PathVariable("keyword") String keyword) {

		log.info("Inside Post Controller searching post with keyword:"+keyword); 
		
		List<PostDto> postList = this.postService.searchPosts(keyword);
		return new ResponseEntity<List<PostDto>>(postList, HttpStatus.OK);
	}

	// post image upload
	@PostMapping("/post/image/upload/{postId}")
	public ResponseEntity<PostDto> uploadPostImage(@RequestParam("image") MultipartFile image,
			@PathVariable("postId") Integer id) throws IOException {

		log.info("Inside Post Controller uploading post image with id:"+id); 
		
		PostDto postDto = this.postService.getPostById(id);
		String name = this.fileService.uploadImage(path, image);
	
		postDto.setImageName(name);

		PostDto updatedpost = this.postService.updatePost(postDto, id);
		return new ResponseEntity<PostDto>(updatedpost, HttpStatus.OK);

	}
	
	//method to serve file
	@GetMapping(value="/post/image/{imageName}",produces = MediaType.IMAGE_JPEG_VALUE)
	public void downloadImage(@PathVariable ("imageName") String imageName,HttpServletResponse response) throws IOException {
		log.info("Inside Post Controller downloading post image with imageName:"+imageName); 
		
		InputStream resource=this.fileService.getResource(path, imageName);
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		StreamUtils.copy(resource,response.getOutputStream());
		
	}

}
