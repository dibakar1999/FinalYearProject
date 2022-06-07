package com.FYP.Controller;

import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import com.FYP.DataTransferObject.ProductDTO;
import com.FYP.Entities.Products;
import com.FYP.Entities.User;
import com.FYP.Global.GlobalData;
import com.FYP.Repository.UserRepository;
import com.FYP.Services.ProductServices;

@Controller
public class CartController {
	
	@Autowired
	UserRepository repository;
	@Autowired
	ProductServices productServices;
	
	
	@GetMapping("/addToCart/{pId}")
	public String addToCart(@PathVariable int pId) {
		GlobalData.cart.add(productServices.getProductById(pId).get());
		
		return "redirect:/bakeryshop/shop";
	}
	
	@GetMapping("/cart")
	public String cartGet(Model model, Principal principal) {
		model.addAttribute("cartCount", GlobalData.cart.size());
		model.addAttribute("total",GlobalData.cart.stream().mapToDouble(Products::getPrice).sum());
		model.addAttribute("carts",GlobalData.cart);
		try {
			String name = principal.getName();
			User username = repository.findUserByEmail(name);
			if(username==null) {
				model.addAttribute("username","User");
			}else {
				model.addAttribute("username", username.getFirstName());
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return "cart";
	}
	
	@GetMapping("/Cart/remove/{pId}")
	public String cartRemover(@PathVariable("pId") Integer pId) {
		int index=-1;
		for(int i=0;i<GlobalData.cart.size();i++)
		{
			
			if(GlobalData.cart.get(i).getPId()==pId)
			{
				
				index=i;
			}
		}
		GlobalData.cart.remove(index);
		
		
		return "redirect:/cart";
	}
	

}
