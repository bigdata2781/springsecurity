package com.example.springsecurity;

import org.springframework.stereotype.Repository;

@Repository
public class UserDao {
	public User getUser(String username) {
		User user = new User();
		user.setUsername("naveen");
		user.setPassword("naveen");
		user.setRole("ROLE_ADMIN");
		return user;
	}
}
