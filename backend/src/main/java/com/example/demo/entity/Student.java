package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
public class Student {

	@Id
	private Integer id;
	private String name;
	private String dob;
	private Integer phone_number;
	private String address;
	private String email;
	
	
	
	public Student() {
		
		// TODO Auto-generated constructor stub
	}



	public Student(Integer id, String name, String dob, Integer phone_number, String address, String email) {
		this.id = id;
		this.name = name;
		this.dob = dob;
		this.phone_number = phone_number;
		this.address = address;
		this.email = email;
	}



	public Integer getId() {
		return id;
	}



	public void setId(Integer id) {
		this.id = id;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getDob() {
		return dob;
	}



	public void setDob(String dob) {
		this.dob = dob;
	}



	public Integer getPhone_number() {
		return phone_number;
	}



	public void setPhone_number(Integer phone_number) {
		this.phone_number = phone_number;
	}



	public String getAddress() {
		return address;
	}



	public void setAddress(String address) {
		this.address = address;
	}



	public String getEmail() {
		return email;
	}



	public void setEmail(String email) {
		this.email = email;
	}
	
	
	
}
