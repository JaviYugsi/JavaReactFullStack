package com.javiapp.reactstack.student;

import com.javiapp.reactstack.exception.ApiRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

//docker start reactFSpostgres
@RestController
@RequestMapping("api/students")
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {

        this.studentService = studentService;
    }

    @GetMapping
   public List<Student> getAllStudents() {

        return studentService.getAllStudents();
    }


    @GetMapping(path = "{studentId}/courses")
    public List<StudentCourse> getAllCoursesForStudents(@PathVariable("studentId") UUID studentId) {



        System.out.println(studentId);
        return studentService.getAllCoursesForStudents(studentId);
    }
    @PostMapping
    public void addNewStudent(@RequestBody @Valid Student student) {

        studentService.addNewStudent(student);
    }
}
