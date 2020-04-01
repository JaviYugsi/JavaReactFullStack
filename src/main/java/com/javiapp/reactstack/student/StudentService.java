package com.javiapp.reactstack.student;

import com.javiapp.reactstack.EmailValidator;
import com.javiapp.reactstack.exception.ApiRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class StudentService {

    private final StudentDataAccessService studentDataAccessService;
    private final EmailValidator emailValidator;

    @Autowired
    public StudentService(StudentDataAccessService studentDataAccessService, EmailValidator emailValidator) {
        this.studentDataAccessService = studentDataAccessService;
        this.emailValidator = emailValidator;
    }

     List<Student> getAllStudents() {

        return studentDataAccessService.selectAllStudents();
    }

    List<StudentCourse> getAllCoursesForStudents (UUID studentId) {


        return studentDataAccessService.selectAllStudentCourses(studentId);
    }
    void addNewStudent(Student student) {

            addNewStudent(null, student);

    }
     void addNewStudent(UUID studentId, Student student) {
         UUID newStudentId = Optional.ofNullable(studentId).orElse(UUID.randomUUID());


         //TODO: Validate email
         if(!emailValidator.test(student.getEmail())) {
            throw new ApiRequestException(student.getEmail() + " is not valid");
         }
         //TODO: Verify that email is not taken

         if(studentDataAccessService.isEmailTaken(student.getEmail())) {
            throw new ApiRequestException(student.getEmail() + " is already taken");
         }

         studentDataAccessService.insertStudent(newStudentId, student);
    }
}
