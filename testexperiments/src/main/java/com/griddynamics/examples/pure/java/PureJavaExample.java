package com.griddynamics.examples.pure.java;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.griddynamics.examples.simple.model.Student;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

/**
 * Here is how we could send requests using "pure" (more or less) Java code w/o third-party components
 */
public class PureJavaExample {

    private static final Logger logger = Logger.getLogger(PureJavaExample.class.getName());


    public static void main(String[] args) throws IOException {

        PureJavaExample example = new PureJavaExample();

        // firstly check that we are "true-coders"
        example.thePurestExample();

        // then let's check that we are not crazy (well, maybe a little bit)
        example.notThePurestExampleButStilPureEnough();
    }

    private void thePurestExample() throws IOException {
        // just a plain string (we don't use any serialisation frameworks here
        String bodyAsString = "{\"id\":\"uniqueId2\",\"name\":\"Petrov\"}";
        sendStudentAsStringToService(bodyAsString);
    }

    private void sendStudentAsStringToService(String studentAsString) throws IOException {
        String contentType = "application/json";

        // Even though it's a pure java you could use properties and not hardcode service urls
        URL u = new URL("http://localhost:8080/student");
        HttpURLConnection conn = (HttpURLConnection) u.openConnection();

        conn.setDoOutput(true);

        // same as we use in curl or GUI rest client
        conn.setRequestMethod("POST");

        // Well it doesn't look to hard right now (as soon as there is just pair headers here)
        conn.setRequestProperty( "Content-Type", contentType);
        conn.setRequestProperty( "Content-Length", String.valueOf(studentAsString.length()));
        OutputStream os = conn.getOutputStream();
        os.write(studentAsString.getBytes());

        logger.info(String.format("Response status is: %d", conn.getResponseCode()));

        BufferedReader br;

        if (200 <= conn.getResponseCode() && conn.getResponseCode() <= 299) {
            br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
        } else {
            br = new BufferedReader(new InputStreamReader((conn.getErrorStream())));
        }

        StringBuilder sb = new StringBuilder();
        String output;

        while ((output = br.readLine()) != null) {
            sb.append(output);
        }

        logger.info(sb.toString());
    }


    // Well that at-least allows us to check field correctness during compile time
    private void notThePurestExampleButStilPureEnough() throws IOException {
        // that is our miracle-worker, that will serialize POJO into string
        ObjectMapper mapper = new ObjectMapper();

        // that is the POJO, use it as used before (we'll serialize it letter)
        Student student = new Student();
        student.setId("someNewUniqueId");
        student.setDirection(Student.Direction.QA);
        student.setAge((short)18);

        sendStudentAsStringToService(mapper.writeValueAsString(student));

    }
}
