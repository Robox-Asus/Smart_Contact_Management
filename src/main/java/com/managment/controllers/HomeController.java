package com.managment.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.managment.dao.UserRepository;
import com.managment.entities.User;
import com.managment.helpers.Message;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class HomeController {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;

	@GetMapping("/home")
	public String home(Model model) {
		model.addAttribute("title", "Home - Smart Contact Manager");
		return "home";
	}

	@GetMapping("/about")
	public String about(Model model) {
		model.addAttribute("title", "About - Smart Contact Manager");
		return "about";
	}

	@GetMapping("/signup")
	public String singup(Model model, HttpSession session) {
		model.addAttribute("title", "Register - Smart Contact Manager");
		model.addAttribute("user", new User());
		session.removeAttribute("message");
		return "signup";
	}

	@GetMapping("/signin")
	public String singin(Model model, HttpSession session) {
		model.addAttribute("title", "Signin - Smart Contact Manager");
		return "login";
	}

	@PostMapping("/register")
	public String register(@Valid @ModelAttribute("user") User user, BindingResult result, Model model,
			HttpSession session) {

		if (result.hasErrors()) {
			return "signup";
		} else {
			try {

				user.setRole("ROLE_USER");
				user.setEnabled(true);
				user.setImageUrl("default.png");
				user.setPassword(passwordEncoder.encode(user.getPassword()));

				this.userRepository.save(user);
				model.addAttribute("user", new User());
				session.setAttribute("message", new Message("Successfully Created", "alert alert-success"));
				return "signup";

			} catch (Exception e) {
				e.printStackTrace();
				model.addAttribute("user", user);
				session.setAttribute("message",
						new Message("Something Went Wrong " + e.getMessage(), "alert alert-danger"));
				return "signup";
			}
		}

	}

	public UserRepository getUserRepository() {
		return userRepository;
	}

	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
}
