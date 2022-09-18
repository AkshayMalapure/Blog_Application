package com.blog.app.services;

import java.util.List;

import com.blog.app.payloads.CategoryDto;

public interface CategorySevice {

	   CategoryDto  createCategory(CategoryDto category);
	   CategoryDto updateCategory(CategoryDto category,Integer categoryId);
	   CategoryDto getCategoryById(Integer categoryId);
	   List<CategoryDto> getAllCategories();
	   void deleteCategory(Integer categoryId);
}
