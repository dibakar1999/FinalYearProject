package com.FYP.Controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.FYP.DataTransferObject.OrdersDTO;
import com.FYP.Entities.Ingredients;
import com.FYP.Entities.Message;
import com.FYP.Entities.Orders;
import com.FYP.Entities.Products;
import com.FYP.Entities.User;
import com.FYP.Global.GlobalData;
import com.FYP.Repository.UserRepository;
import com.FYP.Services.CategoryServices;
import com.FYP.Services.CustomUserDetailService;
import com.FYP.Services.EmailServices;
import com.FYP.Services.IngredientServices;
import com.FYP.Services.OrderServices;
import com.FYP.Services.ProductServices;

@Controller
public class HomeController {
	
	@Autowired
	CategoryServices categoryServices;
	@Autowired
	ProductServices productServices;
	@Autowired
	IngredientServices ingredientServices;
	@Autowired
	CustomUserDetailService customUserDetailService;
	@Autowired
	OrderServices orderServices;
	@Autowired
	UserRepository userRepository;
	@Autowired
	EmailServices emailServices;
	
	/* Home-page */
	@GetMapping("/bakeryshop")
	public String Home(Model model, Principal principal) {
		model.addAttribute("cartCount", GlobalData.cart.size());
		try {
			String name = principal.getName();
			User username = userRepository.findUserByEmail(name);
			if(username==null) {
				model.addAttribute("username","User");
			}else {
				model.addAttribute("username", username.getFirstName());
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		
		return "home";
	}
	
	
	
	
	/* Shop-page */
	@GetMapping("/bakeryshop/shop")
	public String Shop( Model model,Principal principal) {
		try {
			model.addAttribute("Categories", categoryServices.getCategory());
			model.addAttribute("cartCount", GlobalData.cart.size());
			List<Products> products = productServices.getAllProducts();
			model.addAttribute("products", products);
			
			String name = principal.getName();
			User username = userRepository.findUserByEmail(name);
			if(username==null) {
				model.addAttribute("username","User");
			}else {
				model.addAttribute("username", username.getFirstName());
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return "Shop";
		}
		
		
		return "Shop";
	}
	@GetMapping("/bakeryshop/shop/category/{cId}")
	public String getProductsByProductId(@PathVariable int cId, Model model) {
		model.addAttribute("Categories", categoryServices.getCategory());
		model.addAttribute("cartCount", GlobalData.cart.size());
		model.addAttribute("products",productServices.getAllProductsByCategoryId(cId));
		return "Shop";
	}
	
	@RequestMapping("/bakeryshop/shop/viewDetails/{pId}")
	public String viewDetail(@PathVariable("pId") Integer pId, Model model,Principal principal) {
		try {
			String name = principal.getName();
			User username = userRepository.findUserByEmail(name);
			if(username==null) {
				model.addAttribute("username","User");
			}else {
				model.addAttribute("username", username.getFirstName());
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		  Optional<Products> optional = productServices.getProductById(pId); 
		  Products products = optional.get(); 
		  model.addAttribute("products", products);
		  model.addAttribute("cartCount", GlobalData.cart.size());
		return "details";
	}
	
	
	
	
	/* Ingredients-page */
	@GetMapping("/bakeryshop/ingredients/{page}")
	public String Ingredients(@PathVariable Integer page, Model model, Principal principal){
		model.addAttribute("cartCount", GlobalData.cart.size());
		Pageable pageable = PageRequest.of(page, 8);
		Page<Ingredients> ingredients = ingredientServices.getAllIngredients(pageable);
		model.addAttribute("ingredients", ingredients);
		model.addAttribute("currentPage",page);
		model.addAttribute("totalPage",ingredients.getTotalPages());
		try {
			String name = principal.getName();
			User username = userRepository.findUserByEmail(name);
			if(username==null) {
				model.addAttribute("username","User");
			}else {
				model.addAttribute("username", username.getFirstName());
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return "uIngredient";
	}
	@GetMapping("/bakeryshop/ingredients/details/{iId}")
	public String viewIngredientDetails(@PathVariable Integer iId, Model model, Principal principal) {
		model.addAttribute("cartCount", GlobalData.cart.size());
		Optional<Ingredients> optional = ingredientServices.findById(iId);
		model.addAttribute("IngDetails", optional.get());
		
		try {
			String name = principal.getName();
			User username = userRepository.findUserByEmail(name);
			if(username==null) {
				model.addAttribute("username","User");
			}else {
				model.addAttribute("username", username.getFirstName());
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return "ingredientDetails";
	}
	
	
	@GetMapping("/bakeryshop/about")
	public String AboutPage(Model model) {
		model.addAttribute("cartCount", GlobalData.cart.size());
		return "About";
	}
	
	
	
	
	/* payment */
	@GetMapping("/order/CashOnDelivery")
	public String CashOnDelivery(Model model, Principal principal) {
		/* product details */
		model.addAttribute("cartCount", GlobalData.cart.size());
		model.addAttribute("total",GlobalData.cart.stream().mapToDouble(Products::getPrice).sum());
		model.addAttribute("carts",GlobalData.cart);
		return "CashOnDelivery";
	}

	@PostMapping("/order/CashOnDelivery")
	public String orderProduct(@ModelAttribute("orders") OrdersDTO dto,HttpSession session) {
		Orders orders = new Orders();
		
		orders.setName(dto.getName());
		orders.setAddress(dto.getAddress());
		orders.setCNumber(dto.getCNumber());
		orders.setDeliveryDate(dto.getDeliveryDate());
		orders.setOrderDate(LocalDate.now().toString());
		orders.setStatus("unpaid");
		double sum = GlobalData.cart.stream().mapToDouble(Products::getPrice).sum();
		long value = (new Double(sum)).longValue();
		orders.setTotalAmount(value);
		orders.setEmail(dto.getEmail());
		for(int i = 0; i<GlobalData.cart.size(); i++) {
			long pId = GlobalData.cart.get(i).getPId();
			int PID =(int) pId;
			orders.setProductId(PID);
			orders.setPName(GlobalData.cart.get(i).getName());
			orders.setImage(GlobalData.cart.get(i).getProductImage());
			double weight = GlobalData.cart.get(i).getWeight();
			int pWeight =(int) weight;
			orders.setQuantity(pWeight);
			orderServices.saveOrder(orders);
			String productName = GlobalData.cart.get(i).getName();
			String email = dto.getEmail();
			
			String subject = "Order Confirmation";
			String message = ""
					       + "<div style='border: 1px solid #e2e2e2; padding: 20px;'>"
					       + "<h1>"
					       + "Your product is:"
					       + "<b>" +productName
					       + "</n>"
					       + "</h1>"
					       + "<p>"
					       + "Total Amount:"
					       + "<b>"+sum
					       + "</p>"
					       + "<p>"
					       + "Your order has been confirmed. For any query Please contact at 9812345678."
					       + "</P>"
					       + "</div>";
			String to = email;
			emailServices.sendEmail(subject, message, to);
			
			
		}
		
		session.setAttribute("message", new Message("Order has been placed Successfully. Check mail for more details. Thank You", "success"));
		
		
		GlobalData.cart.clear();
		
		return "redirect:/bakeryshop/shop";
	}
	
	
	
	
	
	@GetMapping("/payment")
	public String paymentPage(Principal principal,Model model) {
		try {
			String name = principal.getName();
			User username = userRepository.findUserByEmail(name);
			if(username==null) {
				model.addAttribute("username","User");
			}else {
				model.addAttribute("username", username.getFirstName());
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		String username = principal.getName();
		model.addAttribute("email", username);
		System.out.println(username);
		
		/* product details */
		model.addAttribute("cartCount", GlobalData.cart.size());
		model.addAttribute("total",GlobalData.cart.stream().mapToDouble(Products::getPrice).sum());
		model.addAttribute("carts",GlobalData.cart);
		return "payment";
	}
	
	
	
	//pay with khalti handler.
	@GetMapping("/khalti")
	public String khaltiRequest(@ModelAttribute("orders") OrdersDTO dtos, Principal principal) {
		Orders order = new Orders();
		String email = principal.getName();
		User user = userRepository.findUserByEmail(email);
		String firstName = user.getFirstName();
		
		order.setName(user.getFirstName());
		order.setOrderDate(LocalDate.now().toString());
		order.setCNumber(user.getPhoneNumber());
		order.setStatus("paid");
		double sum = GlobalData.cart.stream().mapToDouble(Products::getPrice).sum();
		long value = (new Double(sum)).longValue();
		order.setTotalAmount(value);
		order.setEmail(principal.getName());
		for(int i = 0; i<GlobalData.cart.size(); i++) {
			long pId = GlobalData.cart.get(i).getPId();
			int PID =(int) pId;
			order.setProductId(PID);
			order.setPName(GlobalData.cart.get(i).getName());
			order.setImage(GlobalData.cart.get(i).getProductImage());
			double weight = GlobalData.cart.get(i).getWeight();
			int pWeight =(int) weight;
			order.setQuantity(pWeight);
			
			
			String productName = GlobalData.cart.get(i).getName();
			String subject = "Order Confirmation";
			String message = ""
					       + "<div style='border: 1px solid #e2e2e2; padding: 20px;'>"
					       + "<h1>"
					       + "Your product is:"
					       + "<b>" +productName
					       + "</n>"
					       + "</h1>"
					       + "<p>"
					       + "Your order has been confirmed. For any query Please contact at 9812345678."
					       + "</P>"
					       + "</div>";
			String to = email;
			System.out.print(to);
			emailServices.sendEmail(subject, message, to);
			
		}
		orderServices.saveOrder(order);
		GlobalData.cart.clear();
		return "redirect:/payment";
	}
	
	
	
	
	
	//privacy policy handler
	@GetMapping("/privacypolicy")
	public String privacyPolicy(Model model) {
		model.addAttribute("cartCount", GlobalData.cart.size());
		return "privacypolicy";
	}
}
