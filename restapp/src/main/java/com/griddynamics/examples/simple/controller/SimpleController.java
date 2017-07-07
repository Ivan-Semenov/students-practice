package com.griddynamics.examples.simple.controller;

import com.griddynamics.examples.simple.model.Student;
import com.griddynamics.examples.simple.service.StudentStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Very simple Spring MVC controller
 */
@RestController("/simple-app")
public class SimpleController {

    @Autowired
    private StudentStorageService studentStorageService;

    @RequestMapping(
            method = RequestMethod.GET,
            path = "/student",
            produces = "application/json"
    )
    public List<Student> getAllStudents() {
        return studentStorageService.getAll();
    }

    @RequestMapping(
            method = RequestMethod.GET,
            path = "/student/{id}",
            produces = "application/json"
    )
    public ResponseEntity getStudentById(@PathVariable("id") String id) {
        Student student = studentStorageService.getStudent(id);
        if (student != null) {
            return new ResponseEntity(student, HttpStatus.OK);
        } else {
            return new ResponseEntity(
                    "Student with given id is not found",
                    HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(
            method = RequestMethod.POST,
            path = "/student",
            consumes = "application/json",
            produces = "application/json"
    )
    public ResponseEntity saveNewStudent(@RequestBody Student student) {
        if (student.getId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Field 'id' is mandatory");
        }
        if (studentStorageService.saveStudent(student)) {
            return new ResponseEntity(student.getId(), HttpStatus.CREATED);
        } else {
            return new ResponseEntity(
                    "Student with given id already exists",
                    HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @RequestMapping(
            method = RequestMethod.PUT,
            path = "/student/{id}",
            consumes = "application/json",
            produces = "application/json"
    )
    public ResponseEntity saveNewStudent(@PathVariable("id") String id, @RequestBody Student student) {
        student.setId(id);
        studentStorageService.saveOrUpdate(student);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(
            method = RequestMethod.DELETE,
            path = "/student/{id}",
            produces = "application/json"
    )
    public ResponseEntity deleteStudent(@PathVariable("id") String id) {
        Student student = studentStorageService.remove(id);
        if (student != null) {
            return ResponseEntity.status(HttpStatus.OK).body(student);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Can not delete student with as it is not found");
    }

    @RequestMapping(
            method = RequestMethod.DELETE,
            path = "/student",
            produces = "application/json"
    )
    public ResponseEntity deleteStudents() {
        studentStorageService.removeAll();
        return ResponseEntity.status(HttpStatus.OK).body("DB is cleared");
    }

}
