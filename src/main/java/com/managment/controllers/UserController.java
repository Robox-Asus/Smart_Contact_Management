package com.managment.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.managment.dao.UserRepository;
import com.managment.entities.User;

@Controller
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@GetMapping("/user")
	@ResponseBody
	public String getMethodName() {
		User user = new User();
		user.setName("Robox Asus");
		user.setEmail("robox_asus@gmail.com");
		userRepository.save(user);
		return "Working ";
	}

}
