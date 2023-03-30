package com.cos.jwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cos.jwt.model.User;

public interface UserRepository extends JpaRepository<User, Integer>{
	public User findByUsername(String username);
}
