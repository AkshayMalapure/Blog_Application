package com.blog.app.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blog.app.payloads.ApiResponse;
import com.blog.app.payloads.CategoryDto;
import com.blog.app.services.CategoryService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/categories")
@Slf4j
public class CategoryController {

	@Autowired
	private CategoryService categoryService;

	@PostMapping("/")
	public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto) {
        log.info("Inside category controller for creation with category id:"+categoryDto.getCategoryId());
		CategoryDto createCategory = this.categoryService.createCategory(categoryDto);
		return new ResponseEntity<CategoryDto>(createCategory, HttpStatus.CREATED);
	}

	@PutMapping("{categoryId}")
	public ResponseEntity<CategoryDto> updateCategory(@Valid @RequestBody CategoryDto categoryDto,@PathVariable("categoryId") Integer id) {
		log.info("Inside category controller for updation with category id:"+id);
		CategoryDto updateCategory = this.categoryService.updateCategory(categoryDto,id);
		return new ResponseEntity<CategoryDto>(updateCategory,HttpStatus.OK);
	}
	
	@GetMapping("{categoryId}")
	public ResponseEntity<CategoryDto> getCategoryById(@PathVariable ("categoryId") Integer id){
		log.info("Inside category controller fetching category data with category id:"+id);
		return new ResponseEntity<CategoryDto>(this.categoryService.getCategoryById(id),HttpStatus.OK);
		
	}
	
	@GetMapping("/")
	public ResponseEntity<List<CategoryDto>> getAllCategories(){
		log.info("Inside category controller fetching all categories");
		return ResponseEntity.ok(this.categoryService.getAllCategories());
	}
	
	@DeleteMapping("{categoryId}")
	public ResponseEntity<ApiResponse> deleteCategory(@PathVariable("categoryId") Integer id){
		log.info("Inside category controller deleting category with id:"+id);
		this.categoryService.deleteCategory(id);
		return new ResponseEntity<ApiResponse>(new ApiResponse("Category deleted Successfully", true), HttpStatus.OK);
	}
	
}
