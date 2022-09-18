package com.blog.app.payloads;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostDto {
	
	private Integer postId;

	@NotEmpty
	@Size(min = 4, message = "title size should be min 4 chars")
	private String title;

	@NotEmpty
	@Size(min = 10, max = 1000, message = "content size should be min 10 chars and max 1000 chars")
	private String content;
	
	private String imageName;
	private Date addedDate;
	private CategoryDto category;
	private UserDto user;
	private List<CommentDto> comments=new ArrayList<>();

}
