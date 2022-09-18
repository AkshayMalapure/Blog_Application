package com.blog.app.payloads;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CategoryDto {
	
	private Integer categoryId;
	
	@NotEmpty
	@Size(min=4,message="category size should be min 4 chars")
	private String categoryTitle;
	
	@NotEmpty
	@Size(min=10,message="categoryDescription  should be min 10 chars")
	private String categoryDescription;
}
