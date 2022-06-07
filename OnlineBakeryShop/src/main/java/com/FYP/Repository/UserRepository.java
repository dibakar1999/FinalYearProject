package com.FYP.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.FYP.Entities.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	
	User findUserByEmail(String email);
	

}
