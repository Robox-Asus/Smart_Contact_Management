package com.managment.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "users_table")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	@NotBlank(message = "User Name Field can not be empty !!")
	private String name;
	@Column(unique = true)
	@NotBlank(message = "Email Field Cannot be Empty !!")
	@Email(message = "Invalid Email")
	private String email;
	@NotBlank(message = "Password Field Cannot be Empty !!")
	private String password;
	private String role;
	private boolean enabled;
	private String imageUrl;
	@Column(length = 500)
	private String about;
	@AssertTrue(message = "Need to Agree Terms and Conditions.")
	private boolean agree;
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
	private List<Contact> contacts = new ArrayList<>();

	public User() {
		super();
	}

	public User(int id, @NotBlank(message = "User Name can not be empty !!") String name,
			@NotBlank(message = "Email Cannot be Empty !!") @Email(message = "Invalid Email") String email,
			String password, String role, boolean enabled, String imageUrl, String about,
			@AssertTrue(message = "Need to Agree Terms and Conditions.") boolean agree, List<Contact> contacts) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.role = role;
		this.enabled = enabled;
		this.imageUrl = imageUrl;
		this.about = about;
		this.agree = agree;
		this.contacts = contacts;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public List<Contact> getList() {
		return contacts;
	}

	public void setList(List<Contact> contacts) {
		this.contacts = contacts;
	}

	public boolean isAgree() {
		return agree;
	}

	public void setAgree(boolean agree) {
		this.agree = agree;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + ", role=" + role
				+ ", enabled=" + enabled + ", imageUrl=" + imageUrl + ", about=" + about + ", agree=" + agree
				+ ", contacts=" + contacts + "]";
	}

}
