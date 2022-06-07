package com.FYP.Controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.FYP.DataTransferObject.IngredientDTO;
import com.FYP.DataTransferObject.ProductDTO;
import com.FYP.Entities.Category;
import com.FYP.Entities.Ingredients;
import com.FYP.Entities.Message;
import com.FYP.Entities.Orders;
import com.FYP.Entities.Products;
import com.FYP.Entities.User;
import com.FYP.Repository.ProductRepository;
import com.FYP.Repository.UserRepository;
import com.FYP.Services.CategoryServices;
import com.FYP.Services.IngredientServices;
import com.FYP.Services.OrderServices;
import com.FYP.Services.ProductServices;
import com.FYP.Services.UserServices;

@Controller
public class AdminController {
	@Autowired
	private CategoryServices categoryServices;
	@Autowired
	private ProductServices productServices;
	@Autowired
	private IngredientServices ingredientServices;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private OrderServices orderServices;
	
	
	@ModelAttribute
	public void userName(Principal principal, Model model) {
		try {
			String name = principal.getName();
			User username = userRepository.findUserByEmail(name);
			if(username==null) {
				model.addAttribute("username","Admin");
			}else {
				model.addAttribute("username", username.getFirstName());
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}
	
	//Display admin page.
	@GetMapping("/admin")
	public String Dashboard() {
		return "dashboard";
	}
	
	//Display category page.
	  @GetMapping("/admin/categories") 
	  public String Categories( Model model) { 
		  List<Category> category = categoryServices.getCategory();
		  model.addAttribute("categories",category); 
		  return "categories"; 
	}
	
	//Display add category page.
	@GetMapping("/admin/categories/addCategory")
	public String category(Model model) {
		model.addAttribute("category", new Category());
		return "addCategory";
	}
	
	//Add Category
	@PostMapping("/admin/categories/addCategory")
	public String AddCategory(@ModelAttribute("category")Category category, HttpSession session) {
		categoryServices.addCategory(category);
		session.setAttribute("message", new Message(" Successful!!","success"));
		return "redirect:/admin/categories/addCategory";
	}
	//Delete Category
	@GetMapping("/admin/categories/delete/{cId}")
	public String DeleteCategory(@PathVariable("cId")int cId, HttpSession session) {
		categoryServices.deletebyId(cId);
		session.setAttribute("message", new Message("Deleted Successfully!!","success"));
		return "redirect:/admin/categories";
	}
	//Update category
	@GetMapping("/admin/categories/update/{cId}")
	public String UpdateCategory(@PathVariable("cId") int cId, Model model, HttpSession session) {
		Optional<Category> optional = categoryServices.getCategoryById(cId);
		if(optional.isPresent()) {
			model.addAttribute("category", optional.get());
			session.setAttribute("message",new Message("Successfully Updated!!","success"));
			
			return "addCategory";
		}
		return "404";
	}
	
	
	
	
	
	//Display products
	@GetMapping("/admin/products")
	public String getproduct(Model model) {
		List<Products> products = productServices.getAllProducts();
		model.addAttribute("products",products );
		
		return "products";
	}
	
	//display addproduct page.
	@GetMapping("/admin/product/addProduct")
	public String products(Model model) {
		model.addAttribute("productDTO", new ProductDTO());
		model.addAttribute("categories", categoryServices.getCategory());
		return "addProduct";
	}
	
	@PostMapping("/admin/product/addProduct")
	public String AddProduct(@ModelAttribute("productDTO") ProductDTO dto, 
			@RequestParam("pImage") MultipartFile file, HttpSession session) throws IOException {
		Products products = new Products();
		
		products.setPId(dto.getPId());
		products.setName(dto.getName());
		products.setCategory(categoryServices.getCategoryById(dto.getCategory_id()).get());
		products.setPrice(dto.getPrice());
		products.setWeight(dto.getWeight());
		products.setDescription(dto.getDescription());
		
		if(!file.isEmpty()) {
			products.setProductImage(file.getOriginalFilename());
			
			File filePath = new ClassPathResource("static/image").getFile();
			Path paths = Paths.get(filePath.getAbsolutePath()+File.separator+file.getOriginalFilename());
			Files.copy(file.getInputStream(), paths, StandardCopyOption.REPLACE_EXISTING);
			
		}
		productServices.addProduct(products);
		session.setAttribute("message", new Message("Data Successfully Saved!!!","success"));
		return "redirect:/admin/product/addProduct";
	}
	
	@GetMapping("/admin/product/delete/{pId}")
	public String deleteProduct(@PathVariable("pId") long pId, HttpSession session) {
		productServices.removeProductById(pId);
		session.setAttribute("message", new Message("Deleted product successfully!!", "success"));
		return "redirect:/admin/products";
	}
	
	@GetMapping("/admin/product/update/{pId}")
	public String updateProduct(@PathVariable("pId") long pId,Model model, HttpSession httpSession) {
		Products products = productServices.getProductById(pId).get();
		ProductDTO dto = new ProductDTO();
		
		try {
			dto.setPId(products.getPId());
			dto.setName(products.getName());
			dto.setCategory_id(products.getCategory().getCId());
			dto.setPrice(products.getPrice());
			dto.setDescription(products.getDescription());
			dto.setProductImage(products.getProductImage());
			model.addAttribute("categories", categoryServices.getCategory());
			model.addAttribute("productDTO", dto);
			model.addAttribute("message","Product has been Updated Successfully!!!");
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		return "addProduct";
	}
	
	
	
	
	
	
	
	/* Ingredients Handler */
	
	@GetMapping("/admin/ingredients")
	public String getIngredient(Model model) {
		model.addAttribute("idto", new IngredientDTO());
		return "addIngredient";
	}
	
	@PostMapping("/admin/ingredient/add")
	public String addIngredient(@Valid @ModelAttribute("idto") IngredientDTO ingredients, BindingResult result ,@RequestParam("pImage") MultipartFile file, HttpSession session) throws IOException{
		Ingredients ing = new Ingredients();
		try {
			if(result.hasErrors()) {
				System.out.println(result);
				return "addIngredient";
			}else {
			ing.setIId(ingredients.getIId());
			ing.setName(ingredients.getName());
			ing.setItems(ingredients.getItems());
			if(!file.isEmpty()) {
				ing.setImage(file.getOriginalFilename());
				
				File filePath = new ClassPathResource("static/image").getFile();
				Path paths = Paths.get(filePath.getAbsolutePath()+File.separator+file.getOriginalFilename());
				Files.copy(file.getInputStream(), paths, StandardCopyOption.REPLACE_EXISTING);
			}
			ingredientServices.addIngredient(ing);
			session.setAttribute("message", new Message("Added Successfully!!", "success"));
			return "redirect:/admin/ingredients";
			}
		} catch (Exception e) {
			// TODO: handle exception
			
			return "redirect:/admin/ingredients";
		}
	}
	
	@GetMapping("/admin/ingredient/{page}")
	public String viewIngredientPage(@PathVariable Integer page, Model model) {
		try {
			Pageable pageable = PageRequest.of(page, 8);
			Page<Ingredients> ingredients = ingredientServices.getAllIngredients(pageable);
			model.addAttribute("Ingredients",ingredients );
			model.addAttribute("currentPage",page);
			model.addAttribute("totalPage",ingredients.getTotalPages());
			return "Ingredients";
		} catch (Exception e) {
			// TODO: handle exception
			return "Ingredients";
		}
	}
	//delete ingredient
	@GetMapping("/admin/ingredient/delete/{iId}")
	public String deleteIngredient(@PathVariable int iId, HttpSession session) {
		try {
			ingredientServices.deleteIngredientById(iId);
			session.setAttribute("message", new Message("Deleted Successfully!!", "success"));
			return "redirect:/admin/ingredient/0";
		} catch (Exception e) {
			// TODO: handle exception
			session.setAttribute("message", new Message("Something went wrong !!", "danger"));
			return "redirect:/admin/ingredient/0";
		}
	}
	
	@GetMapping("/admin/ingredient/update/{iId}")
	public String updateIngredient(@PathVariable int iId, Model model, HttpSession session) {
		try {
			Ingredients ingr = ingredientServices.findById(iId).get();
			IngredientDTO idto = new IngredientDTO();
			try {
				idto.setIId(ingr.getIId());
				idto.setName(ingr.getName());
				idto.setItems(ingr.getItems());
				idto.setImage(ingr.getImage());
				
				model.addAttribute("idto",idto);
				session.setAttribute("message",new Message("Success", "success"));
			} catch (Exception e) {
				System.out.println(e);
			}
			return "addIngredient";
		} catch (Exception e) {
			// TODO: handle exception
			return "addIngredient";
		}
	}
	
	
	/* orders handler */
	@GetMapping("/admin/orders/{page}")
	public String viewOrders(@PathVariable Integer page,Model model) {
		try {
			Pageable pageable = PageRequest.of(page, 14);
			Page<Orders> allorder = orderServices.getAllorder(pageable);
			model.addAttribute("orders",allorder);
			model.addAttribute("currentPage",page);
			model.addAttribute("totalPage",allorder.getTotalPages());
			return "orders";
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return "orders";
		}
	}
	
	
	
}
