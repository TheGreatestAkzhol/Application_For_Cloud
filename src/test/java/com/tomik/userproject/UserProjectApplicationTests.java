package com.tomik.userproject;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.InputStreamReader;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserProjectApplicationTests extends AbstractTestNGSpringContextTests {
    @Value("${local.server.port}")
    private int port;
    @Test(invocationCount = 100, threadPoolSize = 10)
    public void simulateWriteTraffic() {
        try {
            String baseUrl = "http://localhost:" + port + "/api";
            HttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(baseUrl + "/set/Akzhol/" + Math.random());
            String jsonData = "{\"requestData\":\"Akzhol\"}";
            StringEntity entity = new StringEntity(jsonData);
            httpPost.setEntity(entity);
            HttpResponse response = httpClient.execute(httpPost);
            System.out.println("Write Request Status Code: " + response.getStatusLine().getStatusCode());
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuilder responseText = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                responseText.append(line);
            }
            System.out.println("Write Request Response Body: " + responseText.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
