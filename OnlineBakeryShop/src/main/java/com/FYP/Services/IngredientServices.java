package com.FYP.Services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.FYP.Entities.Ingredients;
import com.FYP.Repository.IngredientRepository;

@Service
public class IngredientServices {
	
	
	@Autowired
	IngredientRepository ingredientRepository;
	
	public void addIngredient(Ingredients ingredients) {
		ingredientRepository.save(ingredients);
		
	}
	
	public Page<Ingredients> getAllIngredients(Pageable pageable){
		return ingredientRepository.findAll(pageable);
	}
	
	
	public void deleteIngredientById(int iId) {
		ingredientRepository.deleteById(iId);
	}
	
	public Optional<Ingredients>findById(int iId){
		return ingredientRepository.findById(iId);
		
	}

}
