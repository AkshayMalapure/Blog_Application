package com.blog.app.services;

import com.blog.app.payloads.CommentDto;

public interface CommentService {

	 CommentDto createComment(CommentDto commentDto, Integer userId,Integer postId); 
	void deleteComment(Integer commentId);
}
