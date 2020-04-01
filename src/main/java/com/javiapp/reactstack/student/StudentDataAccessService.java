package com.javiapp.reactstack.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class StudentDataAccessService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public StudentDataAccessService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Student> selectAllStudents() {
        String sql = "" +
                "SELECT " +
                " student_id, " +
                " first_name, " +
                " last_name, " +
                " email, " +
                " gender " +
                " FROM student";
        List<Student> students = jdbcTemplate.query(sql, mapStudentFromDb());

        return students;
    }

    public List<StudentCourse> selectAllStudentCourses (UUID studentId) {
        String sql =
                "SELECT " +
                        "student.student_id, " +
                        "course.course_id, " +
                        "course.name, " +
                        "course.description, " +
                        "course.department, " +
                        "course.teacher_name, " +
                        "student_course.start_date, " +
                        "student_course.end_date, " +
                        "student_course.grade " +
                        " FROM student " +
                        "JOIN student_course USING (student_id) " +
                        "JOIN course USING (course_id) " +
                        "WHERE student.student_id =?";

        List<StudentCourse> studentCourses = jdbcTemplate.query(sql,
                new Object[] {studentId},
                mapStudentCourseFromDb());

        return studentCourses;
    }

    int insertStudent(UUID newStudentId, Student student) {

        String sql = "" +
                "INSERT INTO student (" +
                "student_id, " +
                "first_name, " +
                "last_name, " +
                "email, gender) " +
                "VALUES (?, ?, ?, ?, ?::gender)";
        return jdbcTemplate.update(
                sql,
                newStudentId,
                student.getFirstName(),
                student.getLastName(),
                student.getEmail(),
                student.getGender().name().toUpperCase()
        );

    }

    @SuppressWarnings("ConstantConditions")
    boolean isEmailTaken(String email) {
        String sql = "" +
                "SELECT EXISTS ( " +
                " SELECT 1 " +
                " FROM student " +
                " WHERE email = ?" +
                ")";

        return jdbcTemplate.queryForObject(sql,
                new Object[] {email},
                (resultSet, i) -> resultSet.getBoolean(1));
    }

    private RowMapper<Student> mapStudentFromDb() {
        return (resultSet, i) -> {
            String studentIdStr = resultSet.getString("student_id");
            UUID studentId = UUID.fromString(studentIdStr);
            String firstName = resultSet.getString("first_name");
            String lastName = resultSet.getString("last_name");
            String email = resultSet.getString("email");
            String genderStr = resultSet.getString("gender").toUpperCase();
            Student.Gender gender = Student.Gender.valueOf(genderStr);

            return new Student(
                studentId, firstName, lastName, email, gender
            );
        };
    }

    private  RowMapper<StudentCourse> mapStudentCourseFromDb() {

        return (resultSet, i) -> {
            String studentIdStr = resultSet.getString("student_id");
            UUID studentId = UUID.fromString(studentIdStr);
            String courseIdStr = resultSet.getString("course_id");
            UUID courseId = UUID.fromString(courseIdStr);
            String name = resultSet.getString("name");
            String description = resultSet.getString("description");
            String department = resultSet.getString("department");
            String teacherName = resultSet.getString("teacher_name");
            String startDateStr = resultSet.getString("start_date");
            LocalDate startDate = LocalDate.parse(startDateStr);
            String endDateStr = resultSet.getString("end_date");
            LocalDate endDate = LocalDate.parse(endDateStr);
            Integer grade = Optional.ofNullable(resultSet.getString("grade"))
                    .map(Integer::parseInt)
                    .orElse(null);
            //Integer grade = resultSet.getInt("grade");

            return new StudentCourse(
                    studentId,
                    courseId,
                    name,
                    description,
                    department,
                    teacherName,
                    startDate,
                    endDate,
                    grade
            );


        };
    }


}
