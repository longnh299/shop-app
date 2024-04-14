package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Student;
import com.example.demo.repository.StudentRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class StudentService {
	
	@Autowired
	private StudentRepository studentRepo;
	
	public List<Student> getAll(){
		return studentRepo.findAll();
	}
	
	public void save(Student student) {
		studentRepo.save(student);
	}
	
	public Student get(Integer id) {
		return studentRepo.findById(id).get();
	}
	
	public void delete(Integer id) {
		studentRepo.deleteById(id);
	}
	
	

}
