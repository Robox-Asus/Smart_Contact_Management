package com.managment.controllers;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.managment.dao.ContactRepository;
import com.managment.dao.UserRepository;
import com.managment.entities.Contact;
import com.managment.entities.User;
import com.managment.helpers.Message;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ContactRepository contactRepository;

	@ModelAttribute
	public void addCommonData(Model model, Principal principal) {
		String username = principal.getName();
		User user = userRepository.getUserByUserName(username);
		model.addAttribute("user", user);
	}

	@GetMapping("/index")
	public String index() {
		return "user/user_dashboard";
	}

	@GetMapping("/add-contact")
	public String addContact(Model model) {
		model.addAttribute("title", "Add Contact - Smart Contact Manager");
		model.addAttribute("contact", new Contact());
		return "user/add-contact";
	}

	// handler for processing the contact form
	@PostMapping("/process-contact")
	public String processContact(@ModelAttribute Contact contact,
			@RequestParam("profileImage") MultipartFile multiPartFile, Principal principal, HttpSession session) {
		try {
			String name = principal.getName();
			User user = this.userRepository.getUserByUserName(name);

			if (multiPartFile.isEmpty()) {
				System.out.println("file is Empty");
				contact.setImage("Contact.png");
			} else {
				String[] split = contact.getEmail().split("@");

				contact.setImage(split[0] + "_" + multiPartFile.getOriginalFilename());

				File fileObject = new ClassPathResource("static/img").getFile();

				java.nio.file.Path path = Paths.get(fileObject.getAbsolutePath() + File.separator + split[0] + "_"
						+ multiPartFile.getOriginalFilename());

				Files.copy(multiPartFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				System.out.println("Image is uploaded");
			}

			contact.setUser(user);

			user.getList().add(contact);

			this.userRepository.save(user);
			System.out.println("Data :" + contact);
			System.out.println("Contact Image :" + contact.getImage().toString());
			System.out.println("Successfully added to database");

			// message success
			session.setAttribute("message", new Message("Your contact is added !! Add more ", "alert-success"));
		} catch (Exception e) {
			System.out.println("ERROR " + e.getMessage());
			e.printStackTrace();
			// Error Message
			session.setAttribute("message", new Message("Something went wrong !! Try again ", "alert-danger"));
		}
		return "user/add-contact";
	}

	// show contacts handler
	@GetMapping("/show-contacts/{page}")
	public String showContacts(@PathVariable("page") Integer page, Model model, Principal principal) {
		model.addAttribute("title", "View Contacts");

		String name = principal.getName();
		User user = this.userRepository.getUserByUserName(name);

		// Pageable holds info---> currentPage, contacts Per page
		Pageable pageable = PageRequest.of(page, 4);
		Page<Contact> contacts = this.contactRepository.findContactsByUser(user.getId(), pageable);

		model.addAttribute("contacts", contacts);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", contacts.getTotalPages());
		return "user/show-contacts";
	}

	// showing a Specific Contact details
	@GetMapping("/{cId}/contact")
	public String showContactDetail(@PathVariable("cId") Integer cid, Model model, Principal principal) {
		model.addAttribute("title", "Contact Details");
		Optional<Contact> optional = this.contactRepository.findById(cid);
		Contact contact = optional.get();

		// only authorized user can see the required contact
		String name = principal.getName();
		User user = this.userRepository.getUserByUserName(name);
		if (user.getId() == contact.getUser().getId()) {
			model.addAttribute("contact", contact);
			model.addAttribute("title", contact.getName());
		}

		return "user/contact-detail";
	}

	// delete Contact handler
	@GetMapping("/delete/{cId}")
	public String deleteContact(@PathVariable("cId") Integer cid, Model model, Principal principal,
			HttpSession session) {
		Optional<Contact> contactOptional = this.contactRepository.findById(cid);
		Contact contact = contactOptional.get();

		String name = principal.getName();
		User user = this.userRepository.getUserByUserName(name);
		if (user.getId() == contact.getUser().getId()) {
			// assignment remove img
			// contact.getImage()
			user.getList().remove(contact);
			// contact.setUser(null);
			// this.contactRepository.delete(contact);
			this.contactRepository.deleteById(cid);
			session.setAttribute("message", new Message("Contact deleted Successfully...", "alert-success"));
//				return "redirect:/user/show-contacts/0";
		}
		return "redirect:/user/show-contacts/0";
	}

	// Open update form handler
	@PostMapping("/update-contact/{cId}")
	public String updateForm(@PathVariable("cId") Integer id, Model model) {
		model.addAttribute("title", "Update Contact");
		Optional<Contact> optional = this.contactRepository.findById(id);
		Contact contact = optional.get();
		model.addAttribute("contact", contact);
		return "user/update-form";
	}

	// update Contact handler
	@PostMapping("/process-update")
	public String updateHandler(@ModelAttribute Contact contact, Model model, HttpSession session,
			@RequestParam("profileImage") MultipartFile multiPartFile, Principal principal) {
		try {
			// old Contact details
			Contact oldContactDetail = this.contactRepository.findById(contact.getcId()).get();
			if (!multiPartFile.isEmpty()) {
				String[] split = contact.getEmail().split("@");
				// file is not empty so update the file
				// delete old photo
				File deleteFile = new ClassPathResource("static/img").getFile();
				File file = new File(deleteFile, oldContactDetail.getImage());
				file.delete();

				// update new photo
				File fileObject = new ClassPathResource("static/img").getFile();
				Path path = Paths.get(fileObject.getAbsolutePath() + File.separator + split[0] + "_"
						+ multiPartFile.getOriginalFilename());
				Files.copy(multiPartFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				contact.setImage(split[0] + "_" + multiPartFile.getOriginalFilename());

			} else {
				// file is empty
				contact.setImage(oldContactDetail.getImage());
			}
			User user = this.userRepository.getUserByUserName(principal.getName());
			contact.setUser(user);
			this.contactRepository.save(contact);
			session.setAttribute("message", new Message("Your Contact is updated.......", "alert-success"));
			System.out.println("Id :" + contact.getcId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/user/" + contact.getcId() + "/contact";
	}

	// profile page handler
	@GetMapping("/profile")
	public String yourProfilePage(Model model) {
		model.addAttribute("title", "Profile Page");
		return "user/profile";
	}
}
