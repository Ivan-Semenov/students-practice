package com.griddynamics.examples.resteasy;

import com.griddynamics.examples.simple.model.Student;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.UriBuilder;
import java.util.List;
import java.util.logging.Logger;

/**
 * Well that's a bit simpler that you may use on your real projects:
 *  1) No authentication (anyway that is not too hard to add it, just few more lines of code)
 *  2) No parametrization (that's not a good idea to hardcode URLs or something else that depends on certain env)
 */
public class RestEasyProxyClientSimpleUsage {

    private static final Logger logger = Logger.getLogger(RestEasyProxyClientSimpleUsage.class.getName());

    public static void main(String[] args) {
        SimpleClient simpleClient = gimmeSomeMagic("http://localhost:8080");

        // les's see what's saved already
        List<Student> allStudents = simpleClient.getAllStudents();
        // just the objects - easy to use, yeah
        allStudents.forEach(
                student -> logger.warning(student.getId())
        );


        // firstly remove all from storage
        simpleClient.deleteAllStudents();

        allStudents = simpleClient.getAllStudents();
        logger.warning("Number of students in response is " + allStudents.size());


        // now we could publish new student
        Student student = new Student();
        student.setId("mama_says_Im_unique");
        student.setDirection(Student.Direction.QA);
        student.setAge((short)18);

        String idOfCreatedStudent = simpleClient.saveStudent(student);
        logger.warning("We are getting the same as we saved: " + idOfCreatedStudent);


        // wanna kick of this man, but forgot exact id :((
        try {
            simpleClient.deleteStudent("who_said_you_are_unique");
        } catch (NotFoundException notFoundException) {
            // well, we all knew he could cause troubles :((
            logger.warning("What do you expect?!");
        }

        // Oh, got it - he was mama's boy!
        Student deletedStudent = simpleClient.deleteStudent(idOfCreatedStudent);

        // to be sure that's the same we have saved
        logger.warning(String.format("Id : %s, age: %d, direction: %s",
                deletedStudent.getId(), deletedStudent.getAge(), deletedStudent.getDirection()));

        // are you sure we've deleted him (you know he was a trouble guy)
        try {
            simpleClient.getStudents(idOfCreatedStudent);
            throw new AssertionError("How could he left here?!");
        } catch (NotFoundException e) {
            logger.info(e.getMessage());
            logger.warning("Yeah, in your face, bastard!");
        }

    }


    // that is where magic lives
    public static SimpleClient gimmeSomeMagic(String url) {
        ResteasyClient client = new ResteasyClientBuilder().build();
        ResteasyWebTarget target = client.target(UriBuilder.fromPath(url));
        // can you believe it?!
        return target.proxy(SimpleClient.class);
    }
}
