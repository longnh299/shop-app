package com.example.demo.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Student;
import com.example.demo.service.StudentService;

@RestController
public class StudentController {

	@Autowired
	private StudentService studentService;
	
	// get all student
	@GetMapping("/students")
	public List<Student> listAll(){
		return studentService.getAll();
	}
	
	// get student by id
	@GetMapping("/students/{id}")
	public ResponseEntity<Student> getById(@PathVariable("id") Integer id){
		try {
			Student student = studentService.get(id);
			return new ResponseEntity<Student>(student, HttpStatus.OK);
		} catch (NoSuchElementException e) {
			// TODO: handle exception
			return new ResponseEntity<Student>(HttpStatus.NOT_FOUND);
		}
	}
	
	
	// create a student
	@PostMapping("/students")
	public String add(@RequestBody Student student) {
		studentService.save(student);
		
		return "Add student successfully!";
	}
	
	@PutMapping("/students/{id}")
	public ResponseEntity<?> update(@RequestBody Student student, @PathVariable("id") Integer id){
		try {
			Student existStudent = studentService.get(id);
			studentService.save(student);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (NoSuchElementException e) {
			// TODO: handle exception
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			
		}
		
	}
	
	// delete a student
	@DeleteMapping("/students/{id}")
	public String deleteStudent(@PathVariable("id") Integer id) {
		studentService.delete(id);
		
		return "Delete successfully!";
	}
}
