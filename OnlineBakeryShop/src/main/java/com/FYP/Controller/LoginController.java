package com.FYP.Controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.FYP.Entities.Message;
import com.FYP.Entities.Role;
import com.FYP.Entities.User;
import com.FYP.Global.GlobalData;
import com.FYP.Repository.RoleRepository;
import com.FYP.Repository.UserRepository;
import com.FYP.Services.UserServices;

@Controller
public class LoginController {
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	@Autowired
	UserRepository userRepository;
	@Autowired
	private UserServices userServices;
	@Autowired
	RoleRepository roleRepository;
	
	@GetMapping("/login")
	public String login(Model model) {
		GlobalData.cart.clear();
		return "login";
	}
	
	 
	//Register User handler 
	@GetMapping("/register")
	public String registeration() {
		return "register";
	}
	@PostMapping("/register")
	public String signup(@Valid @ModelAttribute("obj") User user ,BindingResult result, HttpSession session, Model model) {
		
			try {
				
				if(result.hasErrors()) {
					System.out.println(result);
					model.addAttribute("message", result);
					session.setAttribute("message", new Message("Something went Wrong. Try again", "danger"));
					return "redirect:/register";
				}
				else
				{
					String password = user.getPassword();
					user.setPassword(bCryptPasswordEncoder.encode(password));
					List<Role> roles = new ArrayList<>();
					roles.add(roleRepository.findById(2).get());
					user.setRoles(roles);
					
					this.userRepository.save(user);
					session.setAttribute("message", new Message("Register Seccessfull!!!","success"));
					return "redirect:/login";
				}
			} catch (Exception e) {
				session.setAttribute("message", new Message("Something went Wrong. Try again", "danger"));
				// TODO: handle exception
				e.printStackTrace();
				return "redirect:/register";
			}	
	}
	
	@ResponseBody
	@GetMapping("/test")
	public String Test(Principal principal) {
		String username = principal.getName();
		System.out.println(username);
		User optional = userRepository.findUserByEmail(username);
		System.out.println(optional.getFirstName());
		return "Test Success";
	}

}
