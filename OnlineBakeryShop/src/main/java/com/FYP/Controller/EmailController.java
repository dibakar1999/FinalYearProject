package com.FYP.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.FYP.Services.EmailServices;

@RestController
public class EmailController {
	@Autowired
	private EmailServices emailServices;
	
	@RequestMapping("/email")
	@ResponseBody
	public String email() {
		System.out.println("Sending mail.........");
		return "Sending email....";
	}
	
	@RequestMapping(value="/sendEmail", method = RequestMethod.GET )
	public ResponseEntity<?> sendEmail(){
		return null;
		
		
		
	}

}
