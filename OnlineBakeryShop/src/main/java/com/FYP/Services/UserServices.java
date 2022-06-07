package com.FYP.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.FYP.Entities.User;
import com.FYP.Repository.UserRepository;

@Service
public class UserServices {
	@Autowired
	private UserRepository repository;
	
	public List<User> getAllUser(){
		return repository.findAll();
	}

}
