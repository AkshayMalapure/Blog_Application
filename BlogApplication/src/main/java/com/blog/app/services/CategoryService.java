package com.blog.app.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blog.app.entities.Category;
import com.blog.app.exceptions.ResourceNotFoundException;
import com.blog.app.payloads.CategoryDto;
import com.blog.app.repositories.CategoryRepo;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CategoryService implements CategorySevice {

	@Autowired
	private CategoryRepo categoryRepo;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public CategoryDto createCategory(CategoryDto category) {
        log.info("Inside category service for creation with category id:"+category.getCategoryId());
		
		Category cat = this.modelMapper.map(category, Category.class);

		Category addedCat = this.categoryRepo.save(cat);

		return this.modelMapper.map(addedCat, CategoryDto.class);

	}

	@Override
	public CategoryDto updateCategory(CategoryDto category, Integer categoryId) {
          
		log.info("Inside category service for updation with category id:"+categoryId);
		
		Category cat = this.categoryRepo.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "Category Id", categoryId));

		cat.setCategoryTitle(category.getCategoryTitle());
		cat.setCategoryDescription(category.getCategoryDescription());

		Category updatedCat = this.categoryRepo.save(cat);

		return this.modelMapper.map(updatedCat, CategoryDto.class);
	}

	@Override
	public CategoryDto getCategoryById(Integer categoryId) {

		log.info("Inside category service for fetching category details with category id:"+categoryId);
		
		Category cat = this.categoryRepo.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "Category Id", categoryId));
		
		return this.modelMapper.map(cat, CategoryDto.class);
	}

	@Override
	public List<CategoryDto> getAllCategories() {
		log.info("Inside category service for fetching all category details");
		
		List<CategoryDto> categoryList=	this.categoryRepo.findAll().stream().map(category->this.modelMapper.map(category, CategoryDto.class)).collect(Collectors.toList());

		return categoryList;
	}

	@Override
	public void deleteCategory(Integer categoryId) {
		log.info("Inside category service for deleting category with id:"+categoryId);
		
		Category cat = this.categoryRepo.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "Category Id", categoryId));
		this.categoryRepo.delete(cat);

	}

}
