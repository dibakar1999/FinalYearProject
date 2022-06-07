package com.FYP.Controller;

import java.util.Random;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.FYP.Entities.Message;
import com.FYP.Entities.User;
import com.FYP.Repository.UserRepository;
import com.FYP.Services.EmailServices;

@Controller
public class ForgotPasswordController {
	@Autowired
	private EmailServices emailServices;
	@Autowired
	UserRepository userRepository;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@GetMapping("/forgotpassword")
	public String forgotPassword() {
		return "forgotPassword";
	}
	@PostMapping("/forgot/getMail")
	public String sendOTP(@RequestParam("email") String email, HttpSession session) {
		//generating OTP PIN
		Random random = new Random(1000);
		int otpPin = random.nextInt(99999);
		
		
		//Sending OTP Through mail.
		String subject = "Forgot Password OTP PIN.";
		String message = ""
				       + "<div style='border: 1px solid #e2e2e2; padding: 20px;'>"
				       + "<h1>"
				       + "OTP PIN is "
				       + "<b>"+otpPin
				       + "</n>"
				       + "</h1>"
				       + "<p>"
				       + "We OnlineBakery shop don't ask any OTP PIN so, don't share it with anyone. Thank you!!"
				       + "</P>"
				       + "</div>";
		String to = email;
		boolean flag = this.emailServices.sendEmail(subject, message, to);
		
		if(flag) {
			session.setAttribute("otp", otpPin);
			session.setAttribute("email", email);
			return "otp";
		}else {
			
			return "forgotPassword";
		}	
	}
	
	@PostMapping("/verifyOTP")
	public String verifyOTP(@RequestParam("OTP") int uOTP, HttpSession session) {
		int votp = (int)session.getAttribute("otp");
		System.out.println(votp);
		System.out.println(uOTP);
		String vemail = (String)session.getAttribute("email");
		if(uOTP==votp) {
			//change password
			User user = this.userRepository.findUserByEmail(vemail);
			if(user==null){
				session.setAttribute("message",new Message("Error: User doesn't exists. Register User.", "danger"));	
				return "otp";
			}
			else {
				
				return "changePassword";
			}
			
			
		}else{
			session.setAttribute("message", new Message("Error: You have entered wrong OTP PIN.","danger"));
			return "otp";
		}
	}
	
	@PostMapping("/changePassword")
	public String changePassword(@RequestParam("newpassword") String newpassword, @RequestParam("confirmpassword") String confirmpassword,HttpSession session) {
		try {
			String mail = (String)session.getAttribute("email");
			User user = this.userRepository.findUserByEmail(mail);
			if(newpassword.equals(confirmpassword)) {
				user.setPassword(this.bCryptPasswordEncoder.encode(newpassword));
				this.userRepository.save(user);
				return "login";
			}else {
				session.setAttribute("message", new Message("Re-Enter Password. New password and Confirm Password Didn't Match!!", "danger"));
				return "changePassword";
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return "changePassword";
	}

}
