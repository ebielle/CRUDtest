package com.example.crud_test_demo.controllers;

import com.example.crud_test_demo.entities.Student;
import com.example.crud_test_demo.repositories.StudentRepository;
import com.example.crud_test_demo.services.StudentService;
import io.micrometer.common.lang.NonNull;
import jakarta.websocket.server.PathParam;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StudentService studentService;

    @PostMapping("")
    public @ResponseBody Student create(@RequestBody Student student) {
        return studentRepository.save(student);
    }

    @GetMapping("/")
    public @ResponseBody List<Student> getList() {
        return studentRepository.findAll();
    }

    @GetMapping("/{id}")
    public @ResponseBody Student getWithId(@PathVariable Integer id) {
        Optional<Student> student = studentRepository.findById(id);
        return student.orElse(null);
    }

    @PutMapping("/{id}")
    public @ResponseBody Student update(@PathVariable Integer id, @RequestBody @NotNull Student student){
        student.setId(id);
        return studentRepository.save(student);
    }

    @PutMapping("/{id}/activation")
    public @ResponseBody Student putActive(@PathVariable Integer id, @RequestParam Boolean working){
        return studentService.setStudentActivationStatus(id, working);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        studentRepository.deleteById(id);
    }
}
