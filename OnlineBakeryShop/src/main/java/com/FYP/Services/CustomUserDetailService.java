package com.FYP.Services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.FYP.Config.CustomUserDetail;
import com.FYP.Entities.User;
import com.FYP.Repository.UserRepository;

@Service
public class CustomUserDetailService implements UserDetailsService {
	@Autowired
	UserRepository userRepository;
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepository.findUserByEmail(email);
		if(user==null) {
			throw new UsernameNotFoundException("User not found.");
		}
		CustomUserDetail customUserDetail = new CustomUserDetail(user);
		return customUserDetail;
	}

	
}
