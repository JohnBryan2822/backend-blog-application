package org.javacoders.blog.services;

import java.util.List;

import org.javacoders.blog.payloads.CategoryDto;
import org.springframework.stereotype.Service;

@Service
public interface CategoryService {
	
	CategoryDto createCategory(CategoryDto categoryDto);
	CategoryDto updateCategory(CategoryDto categoryDto, Integer categoryId);
	void deleteCategory(Integer categoryId);
	CategoryDto getCategory(Integer categoryId);
	List<CategoryDto> getAllCategories();
}
