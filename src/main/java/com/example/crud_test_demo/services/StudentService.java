package com.example.crud_test_demo.services;

import com.example.crud_test_demo.entities.Student;
import com.example.crud_test_demo.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public Student setStudentActivationStatus(Integer studentId, Boolean isActive){
        Optional<Student> student = studentRepository.findById(studentId);
        if(!student.isPresent()) return null;
        student.get().setWorking(isActive);
        return studentRepository.save(student.get());
    }
}
