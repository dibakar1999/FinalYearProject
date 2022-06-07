package com.FYP.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.FYP.Entities.Ingredients;

public interface IngredientRepository extends JpaRepository<Ingredients,Integer > {

}
