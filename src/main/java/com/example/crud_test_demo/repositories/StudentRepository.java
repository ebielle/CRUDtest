package com.example.crud_test_demo.repositories;
//la repository è l'unica classe che può comunicare con il database

import com.example.crud_test_demo.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
}
