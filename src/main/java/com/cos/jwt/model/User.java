package com.cos.jwt.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String username;
	private String password;
	private String roles; // USER,ADMIN
	
	public List<String> getRoleList() {
		if(this.roles.length() > 0) {
			return Arrays.asList(this.roles.split(","));
		} else {
			return new ArrayList<>();
		}
	}
}
