package com.FYP.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.FYP.Entities.Products;
import com.FYP.Services.ProductServices;

@RestController
public class SearchController {
	@Autowired
	private ProductServices productServices;
	@GetMapping("/search/{word}")
	public ResponseEntity<?> searchProduct(@PathVariable("word") String word){
		List<Products> searchList = productServices.search(word);
		return ResponseEntity.ok(searchList);
	}

}
