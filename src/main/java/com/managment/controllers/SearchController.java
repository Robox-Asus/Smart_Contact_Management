package com.managment.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.managment.dao.ContactRepository;
import com.managment.dao.UserRepository;
import com.managment.entities.Contact;
import com.managment.entities.User;

@RestController
public class SearchController {

	@Autowired
	private ContactRepository contactRepository;
	@Autowired
	private UserRepository userRepository;

	@GetMapping(value = "/search/{query}")
	public ResponseEntity<?> search(@PathVariable("query") String query, Principal principal) {
		System.out.println(query);
		String name = principal.getName();
		User user = this.userRepository.getUserByUserName(name);
		List<Contact> contacts = this.contactRepository.findByNameContainingAndUser(query, user);
		return ResponseEntity.ok(contacts);
	}
}
