package com.griddynamics.examples.pure.java;

import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;

/**
 * Here we could see how to invoke endpoints with Apache HttpClient library
 */
public class ApacheHttpClientExample {
    private static final Logger logger = Logger.getLogger(ApacheHttpClientExample.class.getName());


    public static void main(String[] args) throws IOException {
        // we are not using Jackson lib here, but we could :)
        ApacheHttpClientExample example = new ApacheHttpClientExample();

        // all with "classic" http client
        example.someVeryBasicCase();

        // that is an improvement - more readable, less coding - less errors)
        example.theSameWithFluentAPI();

    }

    private void someVeryBasicCase() throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://localhost:8080/student");

        StringEntity entity = new StringEntity("{\"id\":\"notSoUniquieId\",\"name\":\"Ivanov\"}");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);

        HttpResponse response = httpclient.execute(httpPost);
        if (response.getStatusLine().getStatusCode() != 201) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatusLine().getStatusCode());
        }

        // We could easily convert stream to String using Apache IO utils (but let's use native tools)
        BufferedReader br = new BufferedReader(
                new InputStreamReader((response.getEntity().getContent())));

        StringBuilder sb = new StringBuilder();
        String output;

        while ((output = br.readLine()) != null) {
            sb.append(output);
        }

        // well as for our example that isn't too necessary to convert Stream into String
        // (we could send stream for output)
        // but for most real cases we need to save response (somehow)
        logger.info(sb.toString());

        httpclient.getConnectionManager().shutdown();
    }

    // The same requests can be executed using a simpler, albeit less flexible, fluent API (Fluent HC)
    private void theSameWithFluentAPI() throws IOException {
        String response = Request.Post("http://localhost:8080/student")
                .bodyString("{\"id\":\"oneMoreIdThatCouldBeUnique\",\"name\":\"Ivanov\"}", ContentType.APPLICATION_JSON)
                .execute()
                .returnContent().asString();
        logger.info(response);
    }
}
