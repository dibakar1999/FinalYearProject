package com.FYP.Services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.FYP.Entities.Category;
import com.FYP.Repository.CategoryRepository;

@Service
public class CategoryServices {
	@Autowired
	CategoryRepository categoryRepository;
	
	public void addCategory(Category category) {
		categoryRepository.save(category);
	}
	
	public List<Category>getCategory(){
		return categoryRepository.findAll();
	}
	
	
	
	public void deletebyId(int cId) {
		categoryRepository.deleteById(cId);
	}
	
	public Optional<Category>getCategoryById(int cId){
		Optional<Category> optional = categoryRepository.findById(cId);
		return optional;
	}
	
	//search by keyword
	public List<Category> findBykeyword(String keyword){
		return categoryRepository.findByKeyword(keyword);
	}

	
}
