package com.griddynamics.examples.resteasy;

import com.griddynamics.examples.simple.model.Student;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Here are the almost the same annotations that used by developers
 * (actually that is possible to use the same interface, so that you won't have to write your own)
 */
public interface SimpleClient {

    @Path("/student")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    List<Student> getAllStudents();

    @Path("/student/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    Student getStudents(@PathParam("id") String id);

    @Path("/student")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    String saveStudent(Student student);

    @Path("/student/{id}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    void saveStudent(@PathParam("id") String id, Student student);

    @Path("/student/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    Student deleteStudent(@PathParam("id") String id);

    @Path("/student")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    String deleteAllStudents();

}
