package com.blog.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blog.app.payloads.ApiResponse;
import com.blog.app.payloads.CommentDto;
import com.blog.app.services.CommentService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/")
@Slf4j
public class CommentController {

	@Autowired
	private CommentService commentService;

    @PostMapping("/user/{userId}/post/{postId}/comments")
    public ResponseEntity<CommentDto> createComment(@RequestBody CommentDto commentDto,@PathVariable ("userId") Integer userId,@PathVariable ("postId") Integer id){
    	 
    	log.info("Inside  Comment Controller Posting Comment with User id:"+userId+" and Post Id:"+id);
    	
    	CommentDto comment=commentService.createComment(commentDto,userId, id);
    	return new ResponseEntity<CommentDto>(comment,HttpStatus.CREATED);
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<ApiResponse> deleteComment(@PathVariable ("commentId") Integer id){
    	log.info("Inside Comment Controller deleting comment with Comment id"+id);
    	
    	this.commentService.deleteComment(id);
    	return new ResponseEntity<ApiResponse>(new ApiResponse("Comment deleted successfuly",true),HttpStatus.OK);
    }
}
