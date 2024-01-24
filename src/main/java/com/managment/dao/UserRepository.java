package com.managment.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.managment.entities.User;

public interface UserRepository extends JpaRepository<User, Integer> {

}
